import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Storage } from '@ionic/storage';

import {$WebSocket} from 'angular2-websocket/angular2-websocket';

import { BasicService } from './basic.service';

import { NotificationService } from './notification.service';
import { ApplicationService } from './application.service';
import { AuthService } from './auth.service';

import { Application } from '../model/application';
import { Notification } from '../model/notification';

import { CONFIG } from '../app/config';

import * as SockJS from './sockjs.js';

//declare var SockJS:any;

/**
 * This class is a singleton to keep the content of the application synchronized
 */
@Injectable()
export class ContentService extends BasicService{

  //stores all applications and user settings. Key: applicationId
  private applications:Map<string,Application>;
  //stores all notifications received. Key: applicationId
  private notifications:Map<string,Notification[]>;

  
  
  constructor(private notificationService: NotificationService, 
              private applicationService: ApplicationService, 
              private storage: Storage,
              private http: Http,
              private authService: AuthService) {
    super();
  }
  
  public init(): Promise<boolean> {
    console.log('initializing mobify data');
    this.applications = new Map<string,Application>();
    this.notifications = new Map<string,Notification[]>();

    //load all app settings online
    return this.applicationService.getAll()
    .then(allAppSettings => {
      let getAllStored = [];
      for(let appSettings of allAppSettings){
        this.applications.set(appSettings.applicationId, appSettings);
        //get all the locally stored notifications
        let getStored = this.notificationService.getStored(appSettings);
        getAllStored.push(getStored);
        getStored.then(
          notificationsArray => {
            if(notificationsArray.length > 0){
              //set the last message
              let app:Application = this.applications.get(appSettings.applicationId);
              app.lastNotificationText = notificationsArray[notificationsArray.length-1].message;
              app.lastNotificationTimestamp = notificationsArray[notificationsArray.length-1].timestampSent;
            }
            this.notifications.set(appSettings.applicationId, notificationsArray);
          }
        );
      }
      return Promise.all(getAllStored).then(data => {
        //get all the unreceived messages
        let newMessagesReceived: boolean = false;
        return this.notificationService.getUnreceived()
        .then(notifications => {        
          for(let notification of notifications){
            this.receiveNotification(notification);
            newMessagesReceived = true;
          }   
          if(newMessagesReceived){
            this.flushNotifications();
          }
          //before returning, refresh the unread count for every application
          let appsIterator = this.applications.values();
          let result = appsIterator.next();
          while(result.done == false){
              let app: Application = result.value;
              this.refreshUnreadCount(app.applicationId);
              result = appsIterator.next();
          }
          //obtain a ticket to connect in the websocket
          let headers = new Headers();
          headers.append("Authorization", "Bearer " + this.authService.getUser().accessToken);
          return this.http.get(CONFIG.mobify_ws_base_url + "/ticket", { headers: headers })
          .toPromise()
          .then(
            res => {
              if(res.status == 200){
                let wsTicket:string = res.text();
                //connect to the websocket server
                let url:string = CONFIG.mobify_ws_base_url + "/notifications?ticket="+wsTicket;
                
                var sock = new SockJS(url);
                sock.onopen = function() {
                    sock.send('Initializing SockJS connection from Angular2....');
                };

                sock.onmessage = function(e: any) {
                    console.log(e.data);
                };

                sock.onclose = function() {
                };
                return true;
              }else{
                return false;
              }
            }
          )
          .catch(this.handleError);
        });
      });
    })   
    .catch(this.handleError);
     
  }
  
  public getApplications(): IterableIterator<Application> {
    return this.applications.values();
  }

  public getApplicationById(applicationId:string): Application {
    return this.applications.get(applicationId);
  }

  public getNotificationsByApplicationId(applicationId:string): Notification[] {
    return this.notifications.get(applicationId);
  }

  /**
   * Receive a notification
   */
  public receiveNotification(notification: Notification): void{
    //put them in the array
    this.notifications.get(notification.applicationId).unshift(notification);
    let app:Application = this.applications.get(notification.applicationId);
    app.lastNotificationText = notification.message;
    app.lastNotificationTimestamp = notification.timestampSent;
  }

  /**
   * Store the notifications from all apps in the local storage
   */
  public flushNotifications(): void{
    let iter = this.notifications.keys();
    let result = iter.next();
    while(result.done == false){
      let applicationId: string = result.value;
      this.flushNotificationsByApplicationId(applicationId);
      result = iter.next();
    }
  }

  /**
   * Store the notifications from one specific app in the local storage
   */
  public flushNotificationsByApplicationId(applicationId:string): void {
    let notificationsArray: Notification[] = this.notifications.get(applicationId);
    let json = JSON.stringify(notificationsArray);
    this.storage.set('notifications_'+applicationId, json);
    console.log('Stored locally '+notificationsArray.length+' notifications ('+applicationId+')');
  }

  /**
   * Clear all the notifications of an application
   */
  public clearNotifications(applicationId:string): void {
    //clear application settings (unread and last message)
    let app:Application = this.applications.get(applicationId);
    this.refreshUnreadCount(applicationId);
    app.lastNotificationText = null;
    //clear the notifications
    let emptyArray:Notification[] = new Array();
    //clear the array
    this.notifications.set(applicationId, emptyArray);
    //clear the local storage
    this.storage.set('notifications_'+applicationId, JSON.stringify(emptyArray));
  }

  /**
   * Toogle mute an application
   */
  public muteApplication(applicationId:string): void {
    let app:Application = this.applications.get(applicationId);
    if(app.silent){ //unmute
      app.silent = false;
    }else{ //mute
      app.silent = true;
    }
    this.applicationService.updateApplicationSettings(applicationId, app.silent);
  }

  /**
   * refresh unread notifications count for an application
   */
  public refreshUnreadCount(applicationId: string): void{
    let app:Application = this.applications.get(applicationId);
    let notifications = this.notifications.get(applicationId);
    let count = 0;
    for(let notification of notifications){
      if(notification.read != true){
        count++;
      }
    }
    app.unreadNotificationsCount = count;
    console.log('unread count for '+applicationId+' now is '+count);
  }

}
