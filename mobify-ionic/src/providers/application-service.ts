import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import { BasicService } from './basic-service';

import { Application } from '../model/application';
import { User } from '../model/user';

/**
 * This service class connect to the API to get applications data
 */
@Injectable()
export class ApplicationService extends BasicService {

  constructor(public http: Http) {
    super();
  }
  
  public getApplications(user:User):Promise<Application[]> {
    let app1:Application = new Application();
    app1.name = 'Cluster Monitoring';
    app1.icon = 'xxx';
    let app2:Application = new Application();
    app2.name = 'Warehouse Management';
    app2.icon = 'yyy';
    let app3:Application = new Application();
    app3.name = 'Human Resources';
    app3.icon = 'zzz'; 
    
    let mockList:Application[] = [
      app1,app2,app3
    ];
    
    return new Promise<Application[]>(resolve => {
      resolve(mockList);
    })
  }

}
