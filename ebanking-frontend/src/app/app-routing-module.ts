import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Customers} from "./customers/customers";
import {Accounts} from "./accounts/accounts";
import {NewCustomer} from "./new-customer/new-customer";
import { CustomerAccount } from './customer-accounts/customer-accounts';
import { Login } from './login/login';
import { AdminTemplate } from './admin-template/admin-template';
import { AuthentificationGuard } from './guards/authentification-guard';
import { NotAuthorized } from './not-authorized/not-authorized';

const routes: Routes = [
  { path :"login", component : Login},
  {path:"",redirectTo :"/login",pathMatch:"full"},
  {path: "admin",component: AdminTemplate, canActivate:[AuthentificationGuard],
    children:[
      { path :"accounts", component : Accounts},
      { path :"new-customer", component : NewCustomer,canActivate:[AuthentificationGuard],data:{role:"ADMIN"}},
      { path :"customer-accounts/:id", component : CustomerAccount},
      { path :"customers", component : Customers},
      { path :"notAuthorized", component : NotAuthorized}

    ]}

];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
