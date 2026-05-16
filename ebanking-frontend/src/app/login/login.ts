import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Auth } from '../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit{
  formLogin! : FormGroup;

  constructor(private fb:FormBuilder,private authService:Auth,private router:Router ) {}
    ngOnInit(): void {
this.formLogin=this.fb.group({
  username : this.fb.control(""),
  password : this.fb.control("")
})
    }


  handleLogin() {
    let username =this.formLogin.value.username;
    let pwd =this.formLogin.value.password;

    console.log('Form value:', this.formLogin.value);
    this.authService.login(username,pwd).subscribe({
      next : data =>{
       this.authService.loadProfile(data);
       this.router.navigateByUrl("/admin")
      },
      error : err =>{
        console.log(err)
      }
    })
  }
}
