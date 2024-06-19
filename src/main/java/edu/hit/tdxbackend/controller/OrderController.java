package edu.hit.tdxbackend.controller;

import edu.hit.tdxbackend.entity.Order;
import edu.hit.tdxbackend.entity.ResultInfo;
import edu.hit.tdxbackend.entity.User;
import edu.hit.tdxbackend.mapper.ProductMapper;
import edu.hit.tdxbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("/order")
//@SessionAttributes("user")
public class OrderController {
    private final OrderService orderService;
    private final ProductMapper productMapper;

    @Autowired
    public OrderController(OrderService orderService, ProductMapper productMapper) {
        this.orderService = orderService;
        this.productMapper = productMapper;
    }

    /**
     * 获取用户所有订单
     *
     * @param user 用户
     * @return 订单列表
     */
    @GetMapping("/getAllOrders")
//    public ResultInfo getAllOrders(@SessionAttribute(name = "user", required = false) User user) throws IOException {
    public ResultInfo getAllOrders(User user) {
        ResultInfo info = new ResultInfo();
        if (user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户未登录");
            return info;
        }
        List<Order> orders = orderService.getAllOrders();
        if (orders == null) {
            info.setFlag(false);
            info.setErrorMsg("获取订单失败");
        } else {
            info.setFlag(true);
            info.setData(orders);
        }
        return info;
    }

    /**
     * 根据用户ID获取订单
     *
     * @param user   用户
     * @param userId 用户ID
     * @return 订单列表
     * @throws IOException IO异常
     */
    @GetMapping("/getOrdersByUserId")
    public ResultInfo getOrdersByUserId(@SessionAttribute(name = "user", required = false) User user, @RequestParam("uid") Integer userId) throws IOException {
        ResultInfo info = new ResultInfo();
        if (user == null || userId == null) {
            info.setFlag(false);
            info.setErrorMsg("用户未登录或用户ID无效");
            return info;
        }
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (null == orders) {
            info.setFlag(false);
            info.setErrorMsg("获取订单失败");
        } else {
            info.setFlag(true);
            for(Order order : orders){
                order.setOrderItems(orderService.getOrderItemsByOrderId(order.getId()));
            }
            info.setData(orders);
        }
        return info;
    }

    /**
     * 更新订单状态
     *
     * @param user   用户
     * @param oid    订单ID
     * @param status 订单状态
     * @return 更新结果
     * @throws IOException IO异常
     */
    @PostMapping("/updateStatus")
    public ResultInfo updateStatus(@SessionAttribute(name = "user", required = false) User user, @RequestParam("id") int oid,  @RequestParam("status") int status) throws IOException {
        ResultInfo info = new ResultInfo();
        if (user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户未登录");
            return info;
        }
        boolean flag = orderService.updateStatus(oid, status);
        if (flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("更新订单失败");
        }
        return info;
    }

    /**
     * 删除订单
     *
     * @param user 用户
     * @param oid  订单ID
     * @return 删除结果
     * @throws IOException IO异常
     */
    @PostMapping("/deleteOrder")
    public ResultInfo deleteOrder(@SessionAttribute(name = "user", required = false) User user, @RequestBody Integer oid) throws IOException {
        ResultInfo info = new ResultInfo();
        if (user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户未登录");
            return info;
        }
        boolean flag = orderService.deleteOrder(oid);
        if (flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("删除订单失败");
        }
        return info;
    }

    /**
     * 创建订单
     *
     * @param user  用户
     * @param order 订单
     * @return 创建结果
     * @throws IOException IO异常
     */
    @PostMapping("/createOrder")
    public ResultInfo createOrder(@SessionAttribute(name = "user", required = false) User user, @RequestBody Order order) throws IOException {
        ResultInfo info = new ResultInfo();
        if (user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户未登录");
            return info;
        }
        String orderCode = generateOrderCode();
        String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int status = 0;

        order.setOrderCode(orderCode);
        order.setCreateDate(createDate);
        order.setStatus(status);

        boolean flag = orderService.createOrder(order);
        if (flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("创建订单失败");
        }
        return info;
    }

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    private static String generateOrderCode() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String datePrefix = dtf.format(LocalDateTime.now());

        Random rand = new Random();
        int randomSuffix = 1000 + rand.nextInt(9000);

        return datePrefix + randomSuffix;
    }
}
