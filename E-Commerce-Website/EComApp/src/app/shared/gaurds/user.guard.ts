import { Injectable, OnDestroy } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, ResolveEnd, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { UserAuthService } from '../services/api/user-auth.service';
import { UserApiService } from '../services/api/user-api.service';
@Injectable({
    providedIn: 'root'
})
export class UserGuard implements CanActivate {

    constructor(
        private userAuthService: UserAuthService,
        private router: Router,
        private userApiService: UserApiService
    ) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

        if (this.userAuthService.getToken() !== null) {
            const role = route.data['roles'] as Array<string>;

            if (role && role.length>0) {

                // const userRoles = this.userAuthService.getUserRoles();
                // const match = roles.some(role => userRoles.includes(role));
                console.log(role);
                

                const match = this.userApiService.roleMatch(role);

                if (match) {
                    return true;
                } else {
                    this.router.navigate(['/forbidden']);
                    return false;
                }
            }
        }

        this.router.navigate(['/login']);
        return false;
    }

}
