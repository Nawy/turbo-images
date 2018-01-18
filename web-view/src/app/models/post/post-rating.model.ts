
export class PostRatingModel {
  id: string;
  views: boolean;

  constructor(theId, theViews) {
    this.id = theId;
    this.views = theViews;
  }
}
