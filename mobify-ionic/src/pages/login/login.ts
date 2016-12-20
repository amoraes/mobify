import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { LoadingController } from 'ionic-angular';
import { Loading } from 'ionic-angular';

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
        loader.dismissAll();
        if(data == true){
          console.log('User logged in');
          //initialize the data
          this.contentService.init()
          .then
          //set the root page to avoid user going back to login screen
          this.navCtrl.setRoot(HomePage);
        }else{
          console.log('Wrong username or password');
        }
     });
  }

}
