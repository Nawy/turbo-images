export class Comment {
  text : string;
  ups : number;
  downs : number;


  constructor(_text: string, _ups: number, _downs: number) {
    this.text = _text;
    this.ups = _ups;
    this.downs = _downs;
  }
}
