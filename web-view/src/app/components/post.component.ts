/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, OnInit} from '@angular/core'
import { ActivatedRoute, Params } from '@angular/router';
import { Location }               from '@angular/common';

@Component({
  selector: "s-post",
  templateUrl: './../templates/post.template.html',
  styleUrls: ['./../css/post.style.css']
})
export class PostComponent implements OnInit {
  id: number;

  constructor(
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    window.scrollTo(0,0);
    this.route.params.subscribe(
      params => {
        this.id = params['id'];
      }
    )
  }

  goBack(): void {
    this.location.back();
  }
}
