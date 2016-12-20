import { Component } from '@angular/core';

import { NavController, ToastController, LoadingController, Loading } from 'ionic-angular';

import { AuthService } from '../../providers/auth.service';
import { ApplicationService } from '../../providers/application.service';
import { NotificationService } from '../../providers/notification.service';

import { Application } from '../../model/application';
import { Notification } from '../../model/notification';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
  providers: [ ApplicationService, NotificationService ]
})
export class HomePage {
  
  private applications:Application[]; 

  constructor(private navCtrl: NavController, 
    private toastCtrl: ToastController, 
    private loadingCtrl: LoadingController, 
    private authService: AuthService, 
    private applicationService: ApplicationService, 
    private notificationService: NotificationService) {
    
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
    this.notificationService.getUnreceived()
      .then(data => {
        loader.dismissAll();
                
     });
    
    
    /*
    //load all applications 
    applicationService.getApplications(authService.getUser())
      .then((data) => {
        this.applications = data;
      });
     */    
  }
   
   

}
