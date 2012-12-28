package org.tscore.api

import org.scalatest.FunSuite
import net.liftweb.json._
import net.liftweb.mocks.MockHttpServletRequest
import net.liftweb.mockweb.MockWeb
import org.tscore.TestSuite

class TScoreRESTTest extends TestSuite {
  private implicit val formats = net.liftweb.json.DefaultFormats

  private def data =
    """[
      {
        "id": "1234",
        "name": "Tim the Baker",
        "description": "Needs a Kiva Zip loan to bake goodies",
        "quantity": 200
      },
      {
        "id": "4567",
        "name": "Jeremy the social worker",
        "description": "Needs a Kiva Zip loan to help his community",
        "quantity": 800
      }
    ]"""

  test("parse and find") {
    val subjects = parse(data).extract[List[TestSubject]]
    val result = subjects.find(_.id == "4567").toList
    assert(result.length === 1)
    assert(result(0).name === "Jeremy the social worker")
  }


}

case class TestSubject(id: String, name: String, description: String, quantity: Int) {
  override def equals(obj:Any) : Boolean = {
    obj.isInstanceOf[TestSubject] && obj.asInstanceOf[TestSubject].id == id
  }
}
