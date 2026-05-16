import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Navbar } from './navbar/navbar';
import { Accounts } from './accounts/accounts';
import { Customers } from './customers/customers';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NewCustomer } from './new-customer/new-customer';
import { CustomerAccount } from './customer-accounts/customer-accounts';
import { Login } from './login/login';
import { AdminTemplate } from './admin-template/admin-template';
import { AppHttpInterceptor } from './interceptors/app-http-interceptor';
import { NotAuthorized } from './not-authorized/not-authorized';
@NgModule({
  declarations: [
    App,
    Navbar,
    Accounts,
    Customers,
    NewCustomer,
    CustomerAccount,
    Login,
    AdminTemplate,
    NotAuthorized,
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, CommonModule, ReactiveFormsModule],
  providers: [
    provideBrowserGlobalErrorListeners(),
    { provide: HTTP_INTERCEPTORS, useClass: AppHttpInterceptor, multi: true },
  ],
  bootstrap: [App],
})
export class AppModule {}
