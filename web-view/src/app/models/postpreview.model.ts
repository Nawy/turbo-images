/**
 * Created by ermolaev on 5/9/17.
 */

export class PostPreview {
  id: number;
  path: string;
  rating: number;

  constructor(id: number, path: string, rating: number) {
    this.id = id;
    this.path = path;
    this.rating = rating;
  }
}
