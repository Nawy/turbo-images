import { WebViewPage } from './app.po';

describe('web-view App', () => {
  let page: WebViewPage;

  beforeEach(() => {
    page = new WebViewPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
