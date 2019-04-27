const assert = require('assert');
const expect = require('chai').expect;
const main = require('../main')
const got   = require('got');
var http = require('http');
var checkbox_server_url = 'http://192.168.33.100'
var end_point = 'http://192.168.33.100'
describe('Array', function() {
  describe('#indexOf()', function() {
    it('should return -1 when the value is not present', function(){
      assert.equal(-1, [1,2,3].indexOf(4));
    });
  });
});

describe('main', function() {
    describe('#start()', function() {
      it('should start server on port 9001', async () => {

          await main.start();

          const response = await got('http://localhost:9001', {timeout:500})
          describe('Checkbox run check', function () {
              it('Checkbox.io running', function (done) {
                http.get(checkbox_server_url, function (res) {
                  assert.equal(200, res.statusCode);
                  done();
                  });
                });
			  it('Hitting end point of checkbox to verify', function (done) {
				http.get(end_point, function (res) {
				  assert.equal(200, res.statusCode);
				  done();
				  });
				});
            });
			
          // Stop server
          await main.stop();
          expect(response.body).to.include('ok');
      });
    });
});