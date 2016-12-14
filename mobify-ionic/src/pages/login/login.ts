import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';

import { AuthService } from '../../providers/auth-service';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
  providers: [AuthService]
})
export class LoginPage {
  
  private $authService:AuthService;
  
  public username:string = 'admin';
  public password:string = 'admin123';
  
  constructor(public navCtrl:NavController, private authService:AuthService) {
     this.$authService = authService;
  }
  
  login(event){
      this.$authService.login(this.username, this.password)
        .then(data => {
          if(data == true){
            console.log('User logged in');
          }else{
            console.log('Wrong username or password');
          }
        });
  }

}
