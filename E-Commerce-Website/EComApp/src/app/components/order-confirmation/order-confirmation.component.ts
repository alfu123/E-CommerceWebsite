import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-order-confirmation',
  templateUrl: './order-confirmation.component.html',
  styleUrls: ['./order-confirmation.component.css']
})
export class OrderConfirmationComponent implements OnInit {
 
  expectedDel: string | undefined;
  constructor(private activatedRoute: ActivatedRoute) {
  
   }

  ngOnInit(): void {
   this.expectedDel=this.activatedRoute.snapshot.paramMap.get('expectedDel') ?? '0';
   const busiDaysToAdd=parseInt(this.expectedDel,10);

   const today = new Date();

   // Calculate the expected delivery date by adding business days
   const expectedDeliveryDate = this.addBusinessDays(today, busiDaysToAdd);
   this.expectedDel = this.formatDate(expectedDeliveryDate);
   console.log(this.expectedDel);
  }

  addBusinessDays(date: Date, days: number): Date {
    const copy = new Date(date);
    let count = 0;
    while (count < days) {
      copy.setDate(copy.getDate() + 1);
      if (copy.getDay() !== 0 && copy.getDay() !== 6) {
        // Count only weekdays (Monday to Friday)
        count++;
      }
    }
    return copy;
  }

  // Function to format a date as "yyyy-MM-dd"
  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }


}
