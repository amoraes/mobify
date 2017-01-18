import { Component } from '@angular/core';
import { NavController, NavParams, LoadingController, Loading } from 'ionic-angular';

import { ContentService } from '../../providers/content.service';
import { NotificationService } from '../../providers/notification.service';

import { Application } from '../../model/application';
import { Notification } from '../../model/notification';

@Component({
  selector: 'page-notifications',
  templateUrl: 'notifications.html'
})
export class NotificationsPage {

  private application:Application;
  private notifications:Notification[];

  private loader:Loading;

  constructor(private navCtrl: NavController, 
              private navParams: NavParams, 
              private loadingCtrl: LoadingController, 
              private contentService: ContentService,
              private notificationService: NotificationService) {
    //show a message to wait until the server responds with all the unreceived messages
    this.loader = this.loadingCtrl.create({
      content: "Please wait..."
    });

    let applicationId = this.navParams.data.applicationId;
    this.application = this.contentService.getApplicationById(applicationId);
    this.notifications = this.contentService.getNotificationsByApplicationId(applicationId);
    
    //mark unread notifications as read
    let allReq = [];
    for(let notification of this.notifications){
      if(notification.read != true){
        allReq.push(this.notificationService.markAsRead(notification));
      }
    }
    Promise.all(allReq).then(data => {
      if(data != null && data.length > 0){
        for(let res of data){
          if(res == true){
            //at least one change, refresh unread count and save
            this.contentService.refreshUnreadCount(applicationId);
            this.contentService.flushNotificationsByApplicationId(applicationId);
            break;
          }
        }
      }
    });
    
  }

  ionViewDidLoad() {
    console.log('loaded');
    this.loader.dismissAll();
  }



}
