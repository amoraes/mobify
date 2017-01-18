import { Component } from '@angular/core';

import { NavController, ToastController, LoadingController, Loading, ViewController, PopoverController, NavParams } from 'ionic-angular';

import { AuthService } from '../../providers/auth.service';
import { ContentService } from '../../providers/content.service';

import { Application } from '../../model/application';

import { NotificationsPage } from '../notifications/notifications';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  
  private applications: Application[] = new Array(); 
  private loader:Loading;

  constructor(private navCtrl: NavController, 
    private toastCtrl: ToastController, 
    private loadingCtrl: LoadingController, 
    private authService: AuthService, 
    private contentService: ContentService,
    private popoverCtrl: PopoverController
    ) {
    
    //show a welcome message
    let toast = this.toastCtrl.create({
      message: 'Welcome ' + authService.getUser().username + '!',
      duration: 3000
    });
    toast.present();
    
    //show a message to wait until the server responds with all the unreceived messages
    this.loader = this.loadingCtrl.create({
      content: "Please wait..."
    });
    this.loader.present();
    this.updateApplicationsList();

  }

  ionViewDidLoad() {
    this.loader.dismissAll();
  }

  private updateApplicationsList(): void {
    this.applications = new Array();
    let iter = this.contentService.getApplications();
    let result = iter.next();
    while(result.done == false){
      let app: Application = result.value;
      this.applications.push(app);
      result = iter.next();
    }
    console.log(this.applications);
    //sort by date, descending
    this.applications.sort(function(a:Application, b:Application){
      if(a.lastNotificationTimestamp == null || b.lastNotificationTimestamp == null){
        return 1;
      }else if(a.lastNotificationTimestamp.getTime() > b.lastNotificationTimestamp.getTime()){
        return 1;
      }else{
        return -1;
      }
    });
  } 

  public openApplicationPopover(application:Application, event):void {
    let popover = this.popoverCtrl.create(ApplicationSettingsPopover, {
      callback: (action) => {
       console.log(action+' '+application.applicationId); 
       if(action == 'clear'){
         this.contentService.clearNotifications(application.applicationId);
         this.updateApplicationsList();
       }
       else if(action == 'mute'){
         this.contentService.muteApplication(application.applicationId);
            this.updateApplicationsList();
         }
      },
      application: application
    });
    popover.present({
      ev: event,
    });
  }

  public viewNotifications(application:Application, event):void {
    //no notifications in this app
    if(application.lastNotificationText == null){
      let toast = this.toastCtrl.create({
        message: 'No notifications at the moment!',
        duration: 3000
      });
      toast.present();
    }else{
      //move to notifications page
      this.navCtrl.push(NotificationsPage, {
        applicationId: application.applicationId
      });
    }
    
  }

}

@Component({
  template: `
    <ion-list>
      <ion-list-header>{{ application.name }} Options</ion-list-header>
      <button ion-item (click)="action('mute')"> {{ application.silent ? 'Unmute' : 'Mute' }} </button>
      <button ion-item (click)="action('clear')">Clear</button>
    </ion-list>
  `
})
export class ApplicationSettingsPopover {
  private callback:any;
  private application:Application;

  constructor(private viewCtrl: ViewController, private navParams: NavParams) {
    this.callback = this.navParams.get('callback');
    this.application = this.navParams.get('application');
  }

  action(action:string) {
    this.callback(action)
    this.viewCtrl.dismiss();
  }
}
