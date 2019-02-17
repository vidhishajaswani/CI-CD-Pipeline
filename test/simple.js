const assert = require('assert');
const expect = require('chai').expect;
const main = require('../main')
const got   = require('got');

describe('main', function() {
    describe('#start()', function() {
      it('should start server on port 8080', async () => {

          await main.start();

          const response = await got('http://localhost:8080', {timeout:500})
          // Stop server
          await main.stop();
          expect(response.body).to.include('Hi From');
      });
    });
  });
   