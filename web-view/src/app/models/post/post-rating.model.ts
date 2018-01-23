
export class PostRatingModel {
  id: string;
  up: boolean;
  down: boolean;
  view: boolean;

  constructor(theId, theUp, theDown, theView) {
    this.id = theId;
    this.up = theUp;
    this.down = theDown;
    this.view = theView;
  }
}
