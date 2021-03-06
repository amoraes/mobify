import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
//pages
import { LoginPage } from '../pages/login/login';
import { HomePage, ApplicationSettingsPopover } from '../pages/home/home';
import { NotificationsPage } from '../pages/notifications/notifications';

@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    HomePage,
    ApplicationSettingsPopover,
    NotificationsPage
  ],
  imports: [
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    HomePage,
    ApplicationSettingsPopover,
    NotificationsPage
  ],
  providers: [
    {provide: ErrorHandler, useClass: IonicErrorHandler } 
  ]
})
export class AppModule {}
