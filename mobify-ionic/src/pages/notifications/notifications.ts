import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { ContentService } from '../../providers/content.service';

import { Application } from '../../model/application';
import { Notification } from '../../model/notification';

@Component({
  selector: 'page-notifications',
  templateUrl: 'notifications.html'
})
export class NotificationsPage {

  private application:Application;
  private notifications:Notification[];

  constructor(private navCtrl: NavController, private navParams: NavParams, private contentService: ContentService) {
    let applicationId = this.navParams.data.applicationId;
    this.application = this.contentService.getApplicationById(applicationId);
    this.notifications = this.contentService.getNotificationsByApplicationId(applicationId);
  }

  ionViewDidLoad() {
    console.log('Hello NotificationsPage Page');
  }



}
