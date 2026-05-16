import { Component, OnInit } from '@angular/core';
import {CustomerService} from '../services/customer'
import {Observable,catchError,throwError,map} from "rxjs"
import{Customer} from '../model/customer.model'
import {FormBuilder, FormGroup} from "@angular/forms";
import { Router } from '@angular/router';
@Component({
  selector: 'app-customers',
  templateUrl: './customers.html',
  styleUrls: ['./customers.css'],
  standalone:false
})
export class Customers implements OnInit {

  customers!: Observable<Customer[]>;
  errorMessage!:String;
  searchFormGroup :FormGroup | undefined;
  constructor(private customerService: CustomerService,private fb:FormBuilder,private router:Router) {
  }


  ngOnInit() {
    this.searchFormGroup =this.fb.group(
      {
        keyword: this.fb.control("")
      });

this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    let kw =this.searchFormGroup?.value.keyword;

this.customers=this.customerService.searchCustomers(kw).pipe(
      catchError(err=>{
        this.errorMessage=err.message;
        return throwError(err);
      })
    );
  }

  handleDeleteCustomer(c:Customer) {
    let conf =confirm("are you sure?");
    if(!conf) return;
this.customerService.deleteCustomer(c.id).subscribe({
next : (resp) =>{
  this.customers=this.customers.pipe(
    map(data=>{
      let index =data.indexOf(c)
      data.slice(index,1)
return data;

    })
  )
},
  error :err =>{
  console.log(err);
  }

})
  }

  handleCustomerAccounts(customer:Customer) {
this.router.navigateByUrl("/customer-accounts/"+customer.id,{state:customer})
  }
}
