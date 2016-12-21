import { Component } from '@angular/core';

import { NavController, ToastController, LoadingController, Loading } from 'ionic-angular';

import { AuthService } from '../../providers/auth.service';
import { ContentService } from '../../providers/content.service';

import { HomePage } from '../home/home';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class LoginPage {
  
  public username:string = 'user';
  public password:string = 'user123';
  
  constructor(private navCtrl: NavController,
    private toastCtrl: ToastController, 
    private loadingCtrl: LoadingController, 
    private authService: AuthService,
    private contentService: ContentService) {
    
  }
  
  login(event){
    let loader:Loading = this.loadingCtrl.create({
      content: "Please wait..."
    });
    loader.present();
    this.authService.login(this.username, this.password)
      .then(data => {
        if(data == true){
          console.log('User logged in');
          //initialize the data
          this.contentService.init()
          .then(data => {
            if(data == true){
              loader.dismissAll();
              //set the root page to avoid user going back to login screen
              this.navCtrl.setRoot(HomePage);
            }else{
              console.log('Error initializing application data');
              loader.dismissAll();
              //show an error message
              let toast = this.toastCtrl.create({
                message: 'An unexpected error occurred!',
                duration: 3000
              });
              toast.present();
            }
          });
        }else{
          console.log('Wrong username or password');
          loader.dismissAll();
          //show an error message
          let toast = this.toastCtrl.create({
            message: 'Wrong username or password!',
            duration: 3000
          });
          toast.present();
        }
     });
  }

}
