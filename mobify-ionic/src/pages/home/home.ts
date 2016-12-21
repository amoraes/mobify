import { Component } from '@angular/core';

import { NavController, ToastController, LoadingController, Loading } from 'ionic-angular';

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
    private contentService: ContentService
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
    
    
    /*
    //load all applications 
    applicationService.getApplications(authService.getUser())
      .then((data) => {
        this.applications = data;
      });
     */    
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
  }
   
   

}
