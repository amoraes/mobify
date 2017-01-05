import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';

import { NotificationService } from './notification.service';
import { ApplicationService } from './application.service';

import { Application } from '../model/application';
import { ApplicationSettings } from '../model/application-settings';
import { Notification } from '../model/notification';

/**
 * This class is a singleton to keep the content of the application synchronized
 */
@Injectable()
export class ContentService extends BasicService{

  private applications:Map<String,Application>;
  
  constructor(private notificationService: NotificationService, private applicationService: ApplicationService) {
    super();
  }
  
  public init(): Promise<boolean> {
    console.log('initializing mobify data');
    this.applications = new Map<String,Application>();
    //load all app settings
    return this.applicationService.getAll()
    .then(allAppSettings => {
      for(let appSettings of allAppSettings){
        this.applications.set(appSettings.applicationId, appSettings);
      }
      //get all the unreceived messages
      return this.notificationService.getUnreceived()
      .then(notifications => {        
         for(let notification of notifications){
           this.applications.get(notification.applicationId).notifications.push(notification);
           this.applications.get(notification.applicationId).lastMessageText = notification.message;
         }   
         return true;
      });
    })   
    .catch(this.handleError);
     
  }
  
  public getApplications(): IterableIterator<Application> {
    return this.applications.values();
  }

}
