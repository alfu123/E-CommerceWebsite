import { Component, OnInit } from '@angular/core';
import { UserApiService } from 'src/app/shared/services/api/user-api.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  message: string | undefined;
  constructor(private userService:UserApiService) { }

  ngOnInit(): void {
    this.forUser();
  }
  forUser() {
    this.userService.forUser().subscribe(
      (response) => {
        console.log(response);
        this.message = response;
      }, 
      (error)=>{
        console.log(error);
      }
    );
  }

}
