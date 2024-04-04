package com.kursinis.kursinis.fxControllers;

import com.kursinis.kursinis.hibernateControllers.UserHib;
import com.kursinis.kursinis.model.*;
import com.kursinis.kursinis.hibernateControllers.GenericHib;
import com.kursinis.kursinis.tableParameters.productTableParameters;
import com.kursinis.kursinis.tableParameters.userTableParameters;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainShop implements Initializable {

    public ChoiceBox ProductFilter;
    public Label defaultProductFilterLabel;
    public TextArea productDescription;
    public TextField selectType;
    public ListView <OrderItem> orderListView;
    public Button addProduct;
    public Button orderFulfill;
    public Button updateProduct;
    public Button deleteProduct;
    public TextField productPrice;
    public TextField employeeLogin;
    public TextField employeeSurname;
    public TextField employeeName;
    public PasswordField employeePassword;
    public DatePicker employeeBday;
    public CheckBox adminPrivileges;
    public Tab adminUsersTab;
    public Tab adminEmployeesTab;
    public ListView productCartList;
    public Tab productsTab;
    public Tab cartTab;
    public Tab infoTab;

    public Tab adminDashboard;

    public TextField productTitle;
    public TextField productManufacturer;
    public ListView<User> employeeListView;
    public TextField commentTitleField;
    public TextField commentBodyField;

    public TextField loginField;
    public TextField passwordField;
    public TextField nameField;
    public TextField surnameField;
    public TextField addressField;
    public TextField cardNo;
    public RadioButton isManagerRadio;
    public Button addComment;
    public Button editComment;
    public Button deleteComment;
    public Button removeFromCart;
    public Button emptyCart;
    public Button createOrder;
    public ListView <OrderItem> ordersList;
    public ListView <OrderItem> urgentOrdersList;
    public TextField userIDSort;
    public TextField orderStatusSort;
    public Label orderSelectedLabel;
    public DatePicker dateFromSort;
    public TreeView <Review> feedbackTreeView;
    public TextField feedbackTitle;
    public TextField feedbackBody;
    public Button feedbackSendButt;
    public Button feedbackDelButt;
    public Button feedbackEditButt;
    public TableView <userTableParameters> userManagTableView;
    public TableColumn userIDColumn;
    public TableColumn loginColumn;
    public TableColumn NameColumn;
    public TableColumn surnameColumn;
    public TableColumn birthDateColumn;
    public TableColumn cardNumberColumn;
    public TableColumn addressColumn;
    public TableColumn userTypeColumn;
    public Button showChart;
    public TreeView <Comment> commentTreeView;
    public Button submitButt;
    public Button submitButton;
    public CheckBox isAdminCheck;
    public TextField cardNoField;
    public DatePicker bdayField;
    public RadioButton isAdminRadio;
    public RadioButton isEmployeeCheck;
    public RadioButton isCustomerCheck;
    //----------------------------------
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private GenericHib genericHib;
    private UserHib userHib;

    @FXML
    private TableView<productTableParameters> productTable;
    @FXML
    private TableColumn<productTableParameters, Integer> idColumn;
    @FXML
    private TableColumn<productTableParameters, String> nameColumn;
    @FXML
    private TableColumn<productTableParameters, String> descriptionColumn;
    @FXML
    private TableColumn<productTableParameters, String> manufacturerColumn;
    @FXML
    private TableColumn<productTableParameters, String> typeColumn;
    @FXML
    private TableColumn<productTableParameters, Integer> priceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources){


    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        userHib = new UserHib(entityManagerFactory);
        loadData();
    }


    private void setProductFilter(Event event) {
        defaultProductFilterLabel.setText(ProductFilter.getValue().toString());
    }



//----------------------------------Product Table----------------------------------
    public void deleteProduct( ) {
        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Product product = genericHib.getEntityById(Product.class, selectedProduct.getId());
        product.getComments().clear();
        product.setCart(null);
        product.setWarehouse(null);
        genericHib.deleteProduct(product.getId());
        loadData();
    }
    public void selectProduct(MouseEvent mouseEvent) {
        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct!= null) {
                productTitle.setText(selectedProduct.getTitle());
                productDescription.setText(selectedProduct.getDescription());
                productManufacturer.setText(selectedProduct.getManufacturer());
                productPrice.setText(String.valueOf(selectedProduct.getPrice()));
                selectType.setText(selectedProduct.getType());


            }
            loadComments();
        }

    public void updateProduct( ) {
        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Product product = genericHib.getEntityById(Product.class, selectedProduct.getId());
        product.setTitle(productTitle.getText());
        product.setDescription(productDescription.getText());
        product.setManufacturer(productManufacturer.getText());
        product.setType(selectType.getText());
        product.setPrice(Integer.parseInt(productPrice.getText()));
        product.setWarehouse(null);
        genericHib.update(product);
        loadData();
    }

    public void addProduct( ) {

        genericHib.create(new Product(productTitle.getText(), productDescription.getText(), productManufacturer.getText(), selectType.getText(), Integer.parseInt(productPrice.getText()), null));
            loadData();
    }
    private void loadData() {
        genericHib = new GenericHib(entityManagerFactory);
            List<Product> productList = genericHib.getAllRecords(Product.class);
            List<productTableParameters> tableParametersList = new ArrayList<>();


            for (Product product : productList) {
                if (product.getCart() == null) {
                    productTableParameters tableParams = new productTableParameters();
                    tableParams.setId(product.getId());
                    tableParams.setTitle(product.getTitle());
                    tableParams.setDescription(product.getDescription());
                    tableParams.setManufacturer(product.getManufacturer());
                    tableParams.setType(product.getType());
                    tableParams.setPrice(product.getPrice());


                    tableParametersList.add(tableParams);
                }
            }

            // Clear previous data and set cell value factories
            productTable.getItems().clear();
            productTable.getItems().addAll(tableParametersList);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }


    public void loadAllProductsTab(Event event) {
        loadData();
    }

public void populateEmployeeFields(MouseEvent mouseEvent) {
    User selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
    if (selectedEmployee != null) {
        employeeLogin.setText(selectedEmployee.getLogin());
        employeePassword.setText(selectedEmployee.getPassword());
        employeeBday.setValue(selectedEmployee.getBday());
        employeeName.setText(selectedEmployee.getName());
        employeeSurname.setText(selectedEmployee.getSurname());
        if(selectedEmployee.getType().equals("Manager"))
        {
            isManagerRadio.setSelected(true);
        }
        else
        {
            isManagerRadio.setSelected(false);
        }
        adminPrivileges.setSelected(selectedEmployee.isAdmin());

    }
}
    public void deleteEmployeeAccount(ActionEvent actionEvent) {
        User selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        selectedEmployee.getMyComments().forEach(comment -> comment.setUser(null));
        selectedEmployee.setWarehouse(null);
        //setting all user's written feedback to null
        List <Review> reviewList = genericHib.getAllRecords(Review.class);
        for(Review review : reviewList){
            if(review.getUser().getId() == selectedEmployee.getId()){
                review.setUser(null);
                genericHib.update(review);
            }
        }
        genericHib.delete(selectedEmployee);

}

    public void updateEmployeeAccount(ActionEvent actionEvent) {
        User selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        User employee = genericHib.getEntityById(User.class, selectedEmployee.getId());
        employee.setLogin(employeeLogin.getText());
        employee.setPassword(employeePassword.getText());
        employee.setBday(employeeBday.getValue());
        employee.setName(employeeName.getText());
        employee.setSurname(employeeSurname.getText());

        genericHib.update(employee);
        loadUsers();

    }
    public void createEmployeeAccount() {
        if (employeeLogin.getText().isEmpty() || employeePassword.getText().isEmpty() || employeeBday.getValue() == null || employeeName.getText().isEmpty() || employeeSurname.getText().isEmpty()) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Please fill in all the fields!", "All fields are mandatory.");
        } else if (userHib.checkForRepeatingNames(employeeLogin.getText()) != null) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Username already exists!", "Please choose another login");
            return;
        } else {
            if(isManagerRadio.isSelected())
            {
                userHib.createUser(new User(employeeLogin.getText(), employeePassword.getText(), employeeBday.getValue(), employeeName.getText(), employeeSurname.getText(), "Manager", null, null, null));
            }
            else userHib.createUser(new User(employeeLogin.getText(), employeePassword.getText(), employeeBday.getValue(), employeeName.getText(), employeeSurname.getText(), "Employee", null, null, null));
            JavaAlerts.generateAlert(Alert.AlertType.INFORMATION, "Account Creation", "Your account has been created successfully!", "You can now login.");

        }
        loadUsers();
    }

    public void loadAllEmployees(Event event) {
        loadUsers();
    }





    public void commentAdd(MouseEvent mouseEvent) {
        Comment selectedComment = new Comment();
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());
        if(selectedProduct.getComments().isEmpty()) {
            selectedComment = null;
        }
        if(selectedComment==null) {
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, selectedProduct, null);
            genericHib.create(comment);
            loadComments();
        } else if (selectedComment!=null && selectedComment.getParentComment()==null) {
            selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, selectedProduct, selectedComment);
            genericHib.create(comment);
            loadComments();
        }
        else if(selectedComment!=null && selectedComment.getParentComment()!=null) {
            selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, selectedProduct, selectedComment);
            genericHib.create(comment);
            loadComments();
        }
        loadComments();
    }

    public void commentEdit(MouseEvent mouseEvent) {
        Comment selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedComment.getUser().getId() == currentUser.getId()) {
            commentTitleField.setText(selectedComment.getCommentTitle());
            commentBodyField.setText(selectedComment.getCommentBody());
            addComment.setDisable(true);
            deleteComment.setDisable(true);
            submitButton.setDisable(false);
        }
    }
    public void submitComment(MouseEvent mouseEvent) {
        Comment selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedComment.getUser().getId() == currentUser.getId()) {
            selectedComment.setCommentTitle(commentTitleField.getText());
            selectedComment.setCommentBody(commentBodyField.getText());
            genericHib.update(selectedComment);
            commentTitleField.setText("");
            commentBodyField.setText("");
            addComment.setDisable(false);
            deleteComment.setDisable(false);
            submitButton.setDisable(true);
            loadComments();
        }
    }
    public void commentDelete(MouseEvent mouseEvent) {
        Comment selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
        selectedComment.getReplies().forEach(reply -> reply.setParentComment(null));
            genericHib.deleteComment(selectedComment.getId());
            loadComments();

    }
    public void loadComments() {
        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Product product = genericHib.getEntityById(Product.class, selectedProduct.getId());
        commentTreeView.setRoot(new TreeItem<>());
        commentTreeView.setShowRoot(false);
        commentTreeView.getRoot().setExpanded(true);

        List<Comment> mainComments = product.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)
                .collect(Collectors.toList());

        mainComments.forEach(comment -> addTreeItem(comment, commentTreeView.getRoot()));
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            List<Comment> replies = comment.getReplies().stream().filter(reply -> reply.getParentComment() != null && reply.getParentComment().equals(comment)).collect(Collectors.toList());
            replies.forEach(reply -> addTreeItem(reply, treeItem));
        }
    }
    public void selectComment(MouseEvent mouseEvent) {
        Comment selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
        submitButton.setDisable(true);
        commentTitleField.setText(selectedComment.getCommentTitle());
        commentBodyField.setText(selectedComment.getCommentBody());


    }



    public void loadFeedback(){
        OrderItem order = genericHib.getEntityById(OrderItem.class, ordersList.getSelectionModel().getSelectedItem().getId());
        feedbackTreeView.setRoot(new TreeItem<>());
        feedbackTreeView.setShowRoot(false);
        feedbackTreeView.getRoot().setExpanded(true);

        List<Review> mainReview = order.getReviews().stream()
                .filter(feedback -> feedback.getParentReview() == null)
                .collect(Collectors.toList());

        mainReview.forEach(feedback -> addTreeItem(feedback, feedbackTreeView.getRoot()));
    }
    public void selectCart(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null) {
            orderSelectedLabel.setText("Order Selected:" + selectedOrder.getId());
        }
        loadFeedback();
    }
    public void selectFeedback(MouseEvent mouseEvent) {
        submitButt.setDisable(true);
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() != currentUser.getId()) {
            feedbackDelButt.setDisable(true);
            feedbackEditButt.setDisable(true);
            submitButt.setDisable(true);
            feedbackSendButt.setDisable(false);

        }
        else {
            feedbackDelButt.setDisable(false);
            feedbackEditButt.setDisable(false);
            feedbackSendButt.setDisable(false);

        }


    }
    private void addTreeItem(Review review, TreeItem<Review> parentFeedback) {
        TreeItem<Review> treeItem = new TreeItem<>(review);
        parentFeedback.getChildren().add(treeItem);
        if (review.getReviewReply() != null && !review.getReviewReply().isEmpty()) {
            List<Review> replies = review.getReviewReply().stream().filter(reply -> reply.getParentReview() != null && reply.getParentReview().equals(review)).collect(Collectors.toList());
            replies.forEach(reply -> addTreeItem(reply, treeItem));
        }
    }

    public void loadOrder() {
        ordersList.getItems().clear();
        List<OrderItem> orderList = genericHib.getAllRecords(OrderItem.class);
        for(OrderItem order : orderList){
            if(order.getDateCreated().isBefore(LocalDate.now().minusDays(1)) && order.getOrderStatus().equals("Paid")){
                urgentOrdersList.getItems().add(order);
            }
            else {
                ordersList.getItems().add(order);
            }
        }

    }

    public void cancelOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        OrderItem selectedUrgentOrder = urgentOrdersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null && selectedOrder.getOrderStatus().equals("Pending")){
            selectedOrder.setOrderStatus("Cancelled");
            genericHib.update(selectedOrder);
            loadOrders();
        }
        else if(selectedOrder==null && selectedUrgentOrder!=null && selectedUrgentOrder.getOrderStatus().equals("Pending") ){
            selectedUrgentOrder.setOrderStatus("Cancelled");
            genericHib.update(selectedUrgentOrder);
            loadOrders();
        }
    }

    public void fulfillOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null && selectedOrder.getOrderStatus().equals("Confirmed"))
        {
            selectedOrder.setOrderStatus("Fulfilled");
            genericHib.update(selectedOrder);
            loadOrders();
        }
    }



    public void OrderLoadTab(Event event) {
        loadOrders();
        loadOrder();
    }
    public void loadOrders (){
        ordersList.getItems().clear();
        List<OrderItem> orderList = genericHib.getAllRecords(OrderItem.class);
        for(OrderItem order : orderList){
            if(order.getDateCreated().isBefore(LocalDate.now().minusDays(1)) && order.getOrderStatus().equals("Paid") && order.getUser()!=null){
                urgentOrdersList.getItems().add(order);
            }
            else if (order.getUser()!=null){
                ordersList.getItems().add(order);
            }
        }
    }

    public void sortOrders(MouseEvent mouseEvent) {
        List<OrderItem> orderList = genericHib.getAllRecords(OrderItem.class);
        List<OrderItem> sortedList = orderList.stream()
                .filter(getFilterPredicate())
                .collect(Collectors.toList());

        ordersList.getItems().clear();
        ordersList.getItems().addAll(sortedList);
    }

    private Predicate<OrderItem> getFilterPredicate() {
        List<Predicate<OrderItem>> predicates = new ArrayList<>();

        if (!userIDSort.getText().isEmpty()) {
            int userId = Integer.parseInt(userIDSort.getText());
            predicates.add(order -> order.getUser().getId() == userId);
        }

        if (!orderStatusSort.getText().isEmpty()) {
            String orderStatus = orderStatusSort.getText();
            predicates.add(order -> order.getOrderStatus().equals(orderStatus));
        }

        if (dateFromSort.getValue() != null) {
            predicates.add(order -> order.getDateCreated().isAfter(dateFromSort.getValue()));
        }


        return predicates.stream().reduce(Predicate::and).orElse(e -> true);
    }



    public void loadUsers(){
        List<User> userList = genericHib.getAllRecords(User.class);
        List<userTableParameters> tableParametersList = new ArrayList<>();

        for (User user : userList) {
            userTableParameters tableParams = new userTableParameters();
            tableParams.setId(user.getId());
            tableParams.setLogin(user.getLogin());
            tableParams.setName(user.getName());
            tableParams.setSurname(user.getSurname());
            tableParams.setBirthDate(user.getBirthDate().toString());
            tableParams.setCardNo(user.getCardNo());
            tableParams.setAddress(user.getAddress());
            tableParams.setType(user.getType());

            tableParametersList.add(tableParams);
        }

        userManagTableView.getItems().clear();
        userManagTableView.getItems().addAll(tableParametersList);

        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNo"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
    public void confirmOrder(MouseEvent mouseEvent) {
        OrderItem selectedUrgentOrder = urgentOrdersList.getSelectionModel().getSelectedItem();
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null && selectedOrder.getOrderStatus().equals("Paid"))
        {
            selectedOrder.setOrderStatus("Confirmed");
            genericHib.update(selectedOrder);
            loadOrders();
        }
        else if(selectedOrder==null && selectedUrgentOrder!=null && selectedUrgentOrder.getOrderStatus().equals("Paid"))
        {
            selectedUrgentOrder.setOrderStatus("Confirmed");
            genericHib.update(selectedUrgentOrder);
            loadOrders();
        }
    }

    public void openChart(MouseEvent mouseEvent) {
        List<User> usersList = genericHib.getAllRecords(User.class);
        List<OrderItem> orderItemList = genericHib.getAllRecords(OrderItem.class);
        CategoryDataset dataset = createDatasetForUsers(usersList, orderItemList);
        JFreeChart chart = createBarChart(dataset);
        showChart(chart);
    }
    private CategoryDataset createDatasetForUsers(List<User> usersList, List<OrderItem> orderItemList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (User user : usersList) {
            int totalQuantity = 0;
            for (OrderItem orderItem : orderItemList) {
                if (orderItem.getUser().getId() == user.getId() && user.getType().equals("Customer")) {
                    totalQuantity++;
                }
            }


            dataset.addValue(totalQuantity, "Orders made", user.getName());
        }

        return dataset;
    }
    private JFreeChart createBarChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "User made Orders",
                "Users",
                "Total Orders",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
    private void showChart(JFreeChart chart) {
        JFrame frame = new JFrame("Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));

        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }






    public void deleteUser(MouseEvent mouseEvent) {
        userTableParameters selectedUser = userManagTableView.getSelectionModel().getSelectedItem();
        User user = genericHib.getEntityById(User.class, selectedUser.getId());
        user.getMyComments().clear();
        user.setWarehouse(null);
        genericHib.deleteUser(user.getId());
        loadUsers();
    }

    public void feedbackSubmit(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            selectedReview.setCommentTitle(feedbackTitle.getText());
            selectedReview.setCommentBody(feedbackBody.getText());
            genericHib.update(selectedReview);
            feedbackTitle.setText("");
            feedbackBody.setText("");
            submitButt.setDisable(true);
            feedbackDelButt.setDisable(false);
            feedbackEditButt.setDisable(false);
            feedbackSendButt.setDisable(false);
            loadFeedback();
        }
    }

    public void updateUser(ActionEvent actionEvent) {
        userTableParameters selectedUser = userManagTableView.getSelectionModel().getSelectedItem();
        User user = genericHib.getEntityById(User.class, selectedUser.getId());
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() || bdayField.getValue() == null || nameField.getText().isEmpty() || surnameField.getText().isEmpty() || cardNoField.getText().isEmpty() || addressField.getText().isEmpty()) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Please fill in all the fields!", "All fields are mandatory.");
        }
        else {
            if (isAdminRadio.isSelected()) {
                user.setType("Admin");
            } else if (isEmployeeCheck.isSelected()) {
                user.setType("Employee");
            }
            else if (isCustomerCheck.isSelected()) {
                user.setType("Customer");
            }
            user.setLogin(loginField.getText());
            user.setPassword(Hashing.hashing(passwordField.getText()));
            user.setBirthDate(bdayField.getValue());
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            user.setCardNo(cardNoField.getText());
            user.setAddress(addressField.getText());

            genericHib.update(user);
            loadUsers();
        }
    }

    public void selectUser(MouseEvent mouseEvent) {
        userTableParameters selectedUser = userManagTableView.getSelectionModel().getSelectedItem();
        User user = genericHib.getEntityById(User.class, selectedUser.getId());
        if (selectedUser != null) {
            loginField.setText(selectedUser.getLogin());
            passwordField.setText(user.getPassword());
            nameField.setText(selectedUser.getName());
            surnameField.setText(selectedUser.getSurname());
            addressField.setText(selectedUser.getAddress());
            cardNoField.setText(user.getCardNo());
            bdayField.setValue(user.getBirthDate());
            if(selectedUser.getType().equals("Admin"))
            {
                isAdminCheck.setSelected(true);
            }
            else
            {
                isAdminCheck.setSelected(false);
            }
        }

    }
}


