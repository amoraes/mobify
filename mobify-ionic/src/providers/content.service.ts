import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';

import { NotificationService } from './notification.service';
import { ApplicationService } from './application.service';

import { Application } from '../model/application';
import { Notification } from '../model/notification';

/**
 * This class is a singleton to keep the data of the application synchronized
 */
@Injectable()
export class ContentService extends BasicService{

  private applications:Map<String,Application>;
  
  constructor(private notificationService: NotificationService, private applicationService: ApplicationService) {
    super();
  }
  
  public init(): Promise<boolean> {
    console.log('initializing mobify data');
    this.applications = new Map<String,Application>();
    return this.notificationService.getUnreceived()
      .then(notifications => {        
         //get all the unique apps
         let applicationsIds: string[] = new Array();
         for(let n of notifications){
            if(applicationsIds.indexOf(n.applicationId) < 0){
              applicationsIds.push(n.applicationId);
            }
         }
         //load all of them
         let applicationsLoadPromises: Promise<Application>[] = new Array();  
         for(let appId of applicationsIds){
            applicationsLoadPromises.push(this.applicationService.getApplication(appId));
         } 
         //put the results in the map
         Observable
         .forkJoin(applicationsLoadPromises)
          .subscribe( applications => {
              for(let app of applications){
                this.applications.set(app.applicationId, app);
              }
              return true;   
          });
     })
     .catch(this.handleError);
  }
  
  public getApplications(): IterableIterator<Application> {
    return this.applications.values();
  }

}
