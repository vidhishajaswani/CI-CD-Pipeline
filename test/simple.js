const assert = require('assert');
const expect = require('chai').expect;
var http = require('http');
var checkbox_server_url = 'http://192.168.33.100'
describe('Checkbox run check', function () {
              it('Checkbox.io running', function (done) {
                http.get(checkbox_server_url, function (res) {
                  assert.equal(200, res.statusCode);
                  done();
        });
    });
});