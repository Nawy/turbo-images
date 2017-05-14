import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router'
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ButtonsModule } from 'ngx-bootstrap/buttons';

import { AppComponent } from './components/app.component';
import {LoginComponent} from "./components/login.component";
import {SigninComponent} from "./components/signin.component";
import {PostComponent} from "./components/post.component";
import {NavbarComponent} from "./components/navbar.component";
import {ImagesComponent} from "./components/images.component";
import {CommentComponent} from "./components/comment.component";
import {SettingsComponent} from "./components/settings.component";
import {MaterialsComponent} from "./components/materials.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SigninComponent,
    PostComponent,
    NavbarComponent,
    ImagesComponent,
    CommentComponent,
    SettingsComponent,
    MaterialsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BsDropdownModule.forRoot(),
    ButtonsModule.forRoot(),
    RouterModule.forRoot([
      {
        path: "",
        component: ImagesComponent
      },
      {
        path: "new",
        component: LoginComponent
      },
      {
        path: "signin",
        component: SigninComponent
      },
      {
        path: "settings",
        component: SettingsComponent
      },
      {
        path: "materials",
        component: MaterialsComponent
      }
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
