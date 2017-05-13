/**
 * Created by ermolaev on 5/9/17.
 */

export class Image {
  hash: string;
  path: string;
  rating: number;


  constructor(hash: string, path: string, rating: number) {
    this.hash = hash;
    this.path = path;
    this.rating = rating;
  }
}
