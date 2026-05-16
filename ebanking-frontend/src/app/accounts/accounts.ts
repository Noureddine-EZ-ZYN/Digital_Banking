import { Component,OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import { AccountsService } from '../services/accounts';
import { catchError, Observable, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';
@Component({
  selector: 'app-accounts',
  standalone: false,
  templateUrl: './accounts.html',
  styleUrl: './accounts.css',
})
export class Accounts implements OnInit{

  accountFormGroup!:FormGroup;
  currentPage :number =0;
  pageSize :number=5

  accountObervable! :Observable<AccountDetails>

  operationFormGroup! :FormGroup

  errorMessage!:String;

  constructor(private fb : FormBuilder,private accountServise:AccountsService) {

  }


    ngOnInit(): void {
this.accountFormGroup=this.fb.group({
accountId:this.fb.control('')
});
this.operationFormGroup=this.fb.group({
  operationType :this.fb.control(null),
  amount :this.fb.control(null),
  description :this.fb.control(null),
  accountDestination :this.fb.control(null)


})
    }

  handleSearchAccount() {
    let accoutId:String =this.accountFormGroup.value.accountId

this.accountObervable=this.accountServise.getAccount(accoutId,this.currentPage,this.pageSize).pipe(
  catchError(err=>{
    this.errorMessage=err.error.message;
    return throwError(err);
  })
);

  }

  gotoPage(page: number) {
    this.currentPage=page;
    this.handleSearchAccount()
  }

  handleAccountOperation() {
    let accountId :String =this.accountFormGroup.value.accountId;
    let operationType=this.operationFormGroup.value.operationType;
    let amount : number =this.operationFormGroup.value.amount;
    let description :String=this.operationFormGroup.value.description;
    let accountDestination:String=this.operationFormGroup.value.accountDestination;
    if(operationType == 'DEBIT'){
      this.accountServise.debit(accountId,amount,description).subscribe({
        next:(data)=>{
          alert("Success Debit");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:(err)=>{
          console.log(err);
        }
      });

    }else if(operationType=='CREDIT'){
      this.accountServise.credit(accountId,amount,description).subscribe({
        next:(data)=>{
          alert("Success Credit");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:(err)=>{
          console.log(err);
        }
      });


    }
    else if (operationType=='TRANSFER'){
      this.accountServise.transfer(accountId,accountDestination,amount,description).subscribe({
        next:(data)=>{
          alert("Success Transfer");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:(err)=>{
          console.log(err);
        }
      });

    }

  }
}
