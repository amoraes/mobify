import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { ToastController } from 'ionic-angular';

import { AuthService } from '../../providers/auth-service';
import { ApplicationService } from '../../providers/application-service';

import { Application } from '../../model/application';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
  providers: [ ApplicationService ]
})
export class HomePage {
  
  private applications:Application[]; 

  constructor(private navCtrl: NavController, private toastCtrl: ToastController, private authService: AuthService, private applicationService: ApplicationService) {
    let toast = this.toastCtrl.create({
      message: 'Welcome ' + authService.getUser().username + '!',
      duration: 3000
    });
    toast.present();
    //load all applications 
    applicationService.getApplications(authService.getUser())
      .then((data) => {
        this.applications = data;
      });    
  }
   
   

}
