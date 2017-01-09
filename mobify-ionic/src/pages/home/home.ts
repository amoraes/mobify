import { Component } from '@angular/core';

import { NavController, ToastController, LoadingController, Loading, ViewController, PopoverController, NavParams } from 'ionic-angular';

import { AuthService } from '../../providers/auth.service';
import { ContentService } from '../../providers/content.service';

import { Application } from '../../model/application';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  
  private applications: Application[] = new Array(); 

  constructor(private navCtrl: NavController, 
    private toastCtrl: ToastController, 
    private loadingCtrl: LoadingController, 
    private authService: AuthService, 
    private contentService: ContentService,
    private popoverController: PopoverController
    ) {
    
    //show a welcome message
    let toast = this.toastCtrl.create({
      message: 'Welcome ' + authService.getUser().username + '!',
      duration: 3000
    });
    toast.present();
    
    //show a message to wait until the server responds with all the unreceived messages
    let loader:Loading = this.loadingCtrl.create({
      content: "Please wait..."
    });
    loader.present();
    this.updateApplicationsList();
    loader.dismissAll();
    
    console.log(this.applications);
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
      console.log(a);
      if(a.lastNotificationTimestamp.getTime() > b.lastNotificationTimestamp.getTime()){
        return 1;
      }else{
        return -1;
      }
    });
  } 

  public openApplicationPopover(application:Application, event):void{
    let popover = this.popoverController.create(ApplicationSettingsPopover, {
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
      }
    });
    popover.present({
      ev: event,
    });
  }

}

@Component({
  template: `
    <ion-list>
      <ion-list-header>Options</ion-list-header>
      <button ion-item (click)="action('mute')">Mute</button>
      <button ion-item (click)="action('clear')">Clear</button>
    </ion-list>
  `
})
export class ApplicationSettingsPopover {
  callback:any;

  constructor(private viewCtrl: ViewController, private navParams: NavParams) {
    this.callback = this.navParams.get('callback');
  }

  action(action:string) {
    this.callback(action)
    this.viewCtrl.dismiss();
  }
}
