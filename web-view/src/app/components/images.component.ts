/**
 * Created by ermolaev on 5/9/17.
 */
import {Component} from "@angular/core";
import {PostPreview} from "../models/postpreview.model";

@Component({
  selector: "s-images",
  templateUrl: './../templates/images.template.html',
  styleUrls: ['./../css/images.style.css']
})
export class ImagesComponent {

  postPreviews: PostPreview[];

  searchSort: SearchSort;

  constructor() {
    this.searchSort = SearchSort.RATING;

    const count: number = 100;
    this.postPreviews = new Array<PostPreview>(count);
    var pathes: string[] = ["https://i.imgur.com/uuh9RZ9b.jpg", "https://i.imgur.com/djKgENAb.jpg"];

    for (var i = 0; i < count;) {
      var path: string;
      for (path in pathes) {
        this.postPreviews[i] = new PostPreview(i, pathes[path], Math.floor(Math.random() * 100));
        i++;
        if (i >= count) break;
      }
    }
  }

  getClassState(name: string): string {
    if (name.match("RATING") && this.searchSort === SearchSort.RATING) {
      return "active";
    }

    if (name.match("VIEW") && this.searchSort === SearchSort.VIEW) {
      return "active";
    }

    if (name.match("NEW") && this.searchSort === SearchSort.NEW) {
      return "active";
    }

    return "";
  }

  chooseTopRatingState(): void {
    this.searchSort = SearchSort.RATING;
  }

  chooseTopViewState(): void {
    this.searchSort = SearchSort.VIEW;
  }

  chooseNewState(): void {
    this.searchSort = SearchSort.NEW;
  }
}

enum SearchSort {
  RATING,
  VIEW,
  NEW
}

