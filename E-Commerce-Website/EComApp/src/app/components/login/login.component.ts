import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ResolveEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserApiService } from 'src/app/shared/services/api/user-api.service';
import { UserAuthService } from 'src/app/shared/services/api/user-auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
    hidePassword = true;
    loginForm: FormGroup;
    loginError: any = { username: '', password: '' };
    subscriptions: Subscription[] = [];
    returnUrl: string = '/';
    showLoginSpinner: boolean = false;

    navLinks = [
        { path: '', label: 'Login' },
        { path: '../signup', label: 'Signup' },
    ];

    constructor(private fb: FormBuilder, private userApiService: UserApiService, private route: ActivatedRoute, private router: Router,private userAuthService: UserAuthService) {
        // Create new Login Form
        this.loginForm = this.fb.group({
            userName: ['', [Validators.required, Validators.pattern(/^\w+$/)]],
            userPassword: ['', Validators.required]
        });
        
    }

    ngOnInit(): void {
        //navigate to home if user is already logged in
        if (localStorage.getItem('user')) {
            this.router.navigate(['/']);
        }
        // get return URL from current URL
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((subscription) => { subscription.unsubscribe() });
    }

    onSubmit() {
        if (this.loginForm.valid) {
            this.verifyUser();
        }
    }

    verifyUser() {
        // Show Login Spinner
        this.showLoginSpinner = true;
        // login user using User Api
        this.subscriptions.push(this.userApiService.loginUser(this.loginForm.value).subscribe({
            next: (resp:any) => {
                console.log(this.loginForm.value)
                console.log(resp.data.user.role[0].rolename);
                if (resp) {
                    
                    if (resp.status === 'success' && resp.status_code == 200) {

                        this.userAuthService.setRoles(resp.data.user.role);
                        console.log(resp.data.user.role[0]);
                        this.userAuthService.setToken(resp.data.jwtToken);

                        const role = resp.data.user.role[0].roleName;
                        console.log(role);
                        
                        

                        // if (role === 'Admin') {
                        // this.router.navigate(['/admin']);
                        // } else {
                        // this.router.navigate(['/user']);
                        // }

                        // login Success
                        localStorage.setItem("user", JSON.stringify(resp.data.user));
                        this.router.navigateByUrl(this.returnUrl);
                        
                    } else if (resp.status === 'error' && resp.error && resp.status_code == 401) {
                        // Set Errors
                        this.loginError[resp.error.error_field] = resp.error?.message;
                        this.loginForm.controls[resp.error.error_field].setErrors({ invalid: true });
                    } else {
                        alert("Some Error Occurred!");
                    }
                } else {
                    alert("Some Error Occurred!");
                }
            },
            error: (err) => {
                console.error(err);
                alert("Internal Server Error!");
            },
            complete: () => {
                // Hide Login Spinner
                this.showLoginSpinner = false;
            }
        }));
    }
}

