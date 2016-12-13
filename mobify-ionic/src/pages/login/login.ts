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
  
  private username:string;
  private password:string;
  private reconnect:boolean;
  
  constructor(public navCtrl:NavController, private authService:AuthService) {
     this.$authService = authService;
  }
  
  login(event){
    this.$authService.login(this.username, this.password, this.reconnect); 
  }

}
