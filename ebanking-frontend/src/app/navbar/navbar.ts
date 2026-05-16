import { Component, OnInit } from '@angular/core';
import { Auth } from '../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit{
  constructor(public authService:Auth,private router :Router) {
  }
  ngOnInit(): void {
        throw new Error('Method not implemented.');
    }

  handleLogout() {
    this.authService.logout();
    this.router.navigateByUrl("/login");
    this.authService.isAuthenticated

  }
}
