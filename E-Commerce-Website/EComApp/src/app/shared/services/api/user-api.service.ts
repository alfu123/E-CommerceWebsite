import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ApiResponseModel } from '../../models/api-response';
import { UserAuthService } from './user-auth.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UserApiService {
    baseApiUrl = environment.baseApiUrl;
    requestHeader=new HttpHeaders({
        "No-Auth":"True"
    });

    // private showDrawer = new BehaviorSubject(true);
    // currentState = this.showDrawer.asObservable();

    constructor(private httpClient: HttpClient,private userAuthService:UserAuthService) { }

    // changeState(open: boolean) {
    //     this.showDrawer.next(open);
    // }

    // Login User with API
    loginUser(object: Object) {
        return this.httpClient.post<ApiResponseModel>(`${this.baseApiUrl}/auth/login`, object,{headers:this.requestHeader});
    }
    // Signup User
    signupUser(object: Object) {
        return this.httpClient.post<ApiResponseModel>(`${this.baseApiUrl}/auth/signup`, object);
    }

    forUser() {
      return this.httpClient.get(`${this.baseApiUrl}/auth/forUser`, {
        responseType: 'text',
      });
    }


    public roleMatch(allowedRoles:any): boolean {
        let isMatch = false;
        const userRoles: any = this.userAuthService.getRoles();
    
        if (userRoles != null && userRoles) {
          for (let i = 0; i < userRoles.length; i++) {
            for (let j = 0; j < allowedRoles.length; j++) {
              if (userRoles[i].roleName === allowedRoles[j]) {
                isMatch = true;
                return isMatch;
              } else {
                return isMatch;
              }
            }
          }
        }
        return isMatch;
    }
}
