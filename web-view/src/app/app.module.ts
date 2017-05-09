import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './components/app.component';
import {LoginComponent} from "./components/login.component";
import {PostComponent} from "./components/post.component";
import {NavbarComponent} from "./components/navbar.component";
import {ImagesComponent} from "./components/images.component";
import {CommentComponent} from "./components/comment.component";
import {SettingsComponent} from "./components/settings.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    PostComponent,
    NavbarComponent,
    ImagesComponent,
    CommentComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
