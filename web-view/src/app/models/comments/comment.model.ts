export class Comment {
  text : string;
  ups : number;
  downs : number;

  replies : Array<Comment>;


  constructor(_text: string, _ups: number, _downs: number, _replies : Array<Comment>) {
    this.text = _text;
    this.ups = _ups;
    this.downs = _downs;
    this.replies = _replies;
  }
}
