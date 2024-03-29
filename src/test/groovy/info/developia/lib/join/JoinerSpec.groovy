package info.developia.lib.join


import info.developia.collection.utils.join.fixture.shop.Item
import info.developia.collection.utils.join.fixture.shop.Order
import info.developia.collection.utils.join.fixture.warehouse.Stock
import info.developia.lib.join.Joiner
import spock.lang.Specification

import java.util.function.Function

class JoinerSpec extends Specification {

    def "joining by common should return list of common objects"() {
        given:
        Order order = new Order([
                new Item(1, 'article 1'),
                new Item(2, 'article 2'),
                new Item(3, 'article 3'),
                new Item(4, 'article 4'),
                new Item(5, 'article 5'),
        ])
        Stock stock = new Stock([
                new Item(1, 'article 1'),
                new Item(3, 'article 3'),
                new Item(5, 'article 5'),
        ])
        Function<Item, Long> by = (i) -> i.getId()

        when:
        List<Item> result = Joiner.common(order.getItems(), stock.getItems(), by)

        then:
        with(result) {
            size() == 3
            it[0].getId() == 1
            it[1].getId() == 3
            it[2].getId() == 5
        }
    }
}
