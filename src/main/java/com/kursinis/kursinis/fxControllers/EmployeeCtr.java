package com.kursinis.kursinis.fxControllers;

import com.kursinis.kursinis.hibernateControllers.GenericHib;
import com.kursinis.kursinis.hibernateControllers.UserHib;
import com.kursinis.kursinis.model.*;
import com.kursinis.kursinis.tableParameters.productTableParameters;
import com.kursinis.kursinis.tableParameters.userTableParameters;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class EmployeeCtr implements Initializable {

    public TextField productTitle;
    public TextField productManufacturer;
    public Button addProduct;
    public TextField productPrice;
    public Button deleteProduct;
    public Button updateProduct;
    public TableView <productTableParameters> productTable;
    public TextField commentTitleField;
    public TextField commentBodyField;
    public Button addComment;
    public Button editComment;
    public Button deleteComment;
    public TextField selectType;
    public TreeView <Comment> commentTreeView;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn descriptionColumn;
    public TableColumn typeColumn;
    public ListView <OrderItem> ordersList;
    public Tab cartTab;
    public Button orderFulfill;
    public ListView<OrderItem> urgentOrdersList;
    public TextField userIDSort;
    public TextField orderStatusSort;
    public Label orderSelectedLabel;
    public DatePicker dateFromSort;
    public DatePicker dateToSort;
    public TreeView <Review> feedbackTreeView;
    public TextField feedbackTitle;
    public TextField feedbackBody;
    public Button feedbackSendButt;
    public Button feedbackDelButt;
    public Button feedbackRepButt;
    public Tab infoTab;
    public TextField loginField;
    public TextField passwordField;
    public TextField nameField;
    public TextField surnameField;
    public TextField addressField;
    public TextField cardNo;
    public TableView <userTableParameters> userManagTableView;
    public TableColumn userIDColumn;
    public TableColumn loginColumn;
    public TableColumn unameColumn;
    public TableColumn surnameColumn;
    public TableColumn birthDateColumn;
    public TableColumn cardNumberColumn;
    public TableColumn addressColumn;
    public TextField description;
    public TableColumn manufacturerColumn;
    public TableColumn priceColumn;
    public Button submitButton;
    public Button feedbackEdit;
    public Button feedSubmit;
    public Button updateInfo;
    public DatePicker bDateField;
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private GenericHib genericHib;
    private UserHib userHib;
    public Tab productsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources){
    loadProducts();
    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        userHib = new UserHib(entityManagerFactory);

    }

    public void loadAllProductsTab(Event event) {
        loadProducts();
    }
    public void loadProducts(){
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

        productTable.getItems().clear();
        productTable.getItems().addAll(tableParametersList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
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
        if(selectedComment.getUser().getId() == currentUser.getId()) {
            addComment.setDisable(false);
            deleteComment.setDisable(false);
            editComment.setDisable(false);
            commentTitleField.setText(selectedComment.getCommentTitle());
            commentBodyField.setText(selectedComment.getCommentBody());
        }
        else {
            addComment.setDisable(false);
            deleteComment.setDisable(true);
            editComment.setDisable(true);
            commentTitleField.setText("");
            commentBodyField.setText("");
        }
    }
    public void selectProduct(MouseEvent mouseEvent) {
        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct!= null) {
            productTitle.setText(selectedProduct.getTitle());
            description.setText(selectedProduct.getDescription());
            productManufacturer.setText(selectedProduct.getManufacturer());
            productPrice.setText(String.valueOf(selectedProduct.getPrice()));
            selectType.setText(selectedProduct.getType());
        }
        loadComments();
    }
    public void addProduct(ActionEvent actionEvent) {
        genericHib.create(new Product(productTitle.getText(), description.getText(), productManufacturer.getText(), selectType.getText(), Integer.parseInt(productPrice.getText()), null));
        loadProducts();
    }

    public void deleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());
        genericHib.deleteProduct(selectedProduct.getId());
        loadProducts();
    }

    public void updateProduct(ActionEvent actionEvent) {
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());

            selectedProduct.setTitle(productTitle.getText());
            selectedProduct.setDescription(description.getText());
            selectedProduct.setManufacturer(productManufacturer.getText());
            selectedProduct.setType(selectType.getText());
            selectedProduct.setPrice(Integer.parseInt(productPrice.getText()));
            selectedProduct.setWarehouse(null);
            genericHib.update(selectedProduct);
            loadProducts();

    }


    public void commentAdd(MouseEvent mouseEvent) {
        Comment selectedComment = new Comment();
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());
        if(selectedProduct.getComments().isEmpty()) {
            selectedComment = null;
        }
        else {
            selectedComment = commentTreeView.getSelectionModel().getSelectedItem().getValue();
        }
        if(selectedComment==null) {
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, selectedProduct, null);
            genericHib.create(comment);
            loadComments();
        } else if (selectedComment!=null && selectedComment.getParentComment()==null) {
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, selectedProduct, selectedComment);
            genericHib.create(comment);
            loadComments();
        }
        else if(selectedComment!=null && selectedComment.getParentComment()!=null) {
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
        if(selectedComment.getUser().getId() == currentUser.getId()) {
            genericHib.deleteComment(selectedComment.getId());
            loadComments();
        }
    }



    public void OrderLoadTab(Event event) {
        loadOrders();
    }
    public void loadOrders(){
        ordersList.getItems().clear();
        List<OrderItem> orderList = genericHib.getAllRecords(OrderItem.class);
        for(OrderItem order : orderList){
            if(order.getDateCreated().isBefore(LocalDate.now().minusDays(1)) && order.getOrderStatus().equals("Paid")){
                urgentOrdersList.getItems().add(order);
            }
            else if(!order.getUser().equals(null)) {
                ordersList.getItems().add(order);
            }
        }

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
    public void selectFeedback(MouseEvent mouseEvent) {
        submitButton.setDisable(true);
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() != currentUser.getId()) {
            feedbackDelButt.setDisable(true);
            feedbackEdit.setDisable(true);
            feedSubmit.setDisable(true);
            feedbackSendButt.setDisable(false);

        }
        else {
            feedbackDelButt.setDisable(false);
            feedbackEdit.setDisable(false);
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
    public void selectCart(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null) {
            orderSelectedLabel.setText("Order Selected:" + selectedOrder.getId());
        }
        loadFeedback();
    }

    public void cancelOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        OrderItem selectedUrgentOrder = urgentOrdersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null && selectedOrder.getOrderStatus().equals("Pending")){
            selectedOrder.setOrderStatus("Cancelled");
            genericHib.update(selectedOrder);
            loadOrders();
        }
        else if(selectedOrder==null && selectedUrgentOrder!=null && selectedUrgentOrder.getOrderStatus().equals("Pending")){
            selectedUrgentOrder.setOrderStatus("Cancelled");
            genericHib.update(selectedUrgentOrder);
            loadOrders();
        }
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

    public void fulfillOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        if(selectedOrder!=null && selectedOrder.getOrderStatus().equals("Confirmed"))
        {
            selectedOrder.setOrderStatus("Fulfilled");
            genericHib.update(selectedOrder);
            loadOrders();
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



    public void sendFeedback(MouseEvent mouseEvent) {
        Review selectedReview = new Review();
        OrderItem order = genericHib.getEntityById(OrderItem.class, ordersList.getSelectionModel().getSelectedItem().getId());
        if(order.getReviews().isEmpty()) {
            selectedReview = null;
        }
        else {
            selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        }
        if(selectedReview ==null) {
            Review review = new Review(feedbackTitle.getText(), feedbackBody.getText(), currentUser, order,null );
            genericHib.create(review);
            loadFeedback();
        } else if (selectedReview !=null && selectedReview.getParentReview()==null) {
            Review review = new Review(feedbackTitle.getText(), feedbackBody.getText(), currentUser, order, selectedReview);
            genericHib.create(review);
            loadFeedback();
        }
        else if(selectedReview !=null && selectedReview.getParentReview()!=null) {
            Review review = new Review(feedbackTitle.getText(), feedbackBody.getText(), currentUser, order, selectedReview);
            genericHib.create(review);
            loadFeedback();
        }
       loadFeedback();
    }

    public void deleteFeedback(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            genericHib.deleteFeedback(selectedReview.getId());
            loadFeedback();
        }
    }


    public void UserManagementLoadTab(Event event) {
        loadUsers();
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

            tableParametersList.add(tableParams);
        }

        userManagTableView.getItems().clear();
        userManagTableView.getItems().addAll(tableParametersList);

        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        unameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNo"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    public void updateYourInfo(MouseEvent mouseEvent) {
        userHib = new UserHib(entityManagerFactory);
        User user = genericHib.getEntityById(User.class, currentUser.getId());
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty() || surnameField.getText().isEmpty() || addressField.getText().isEmpty() || cardNo.getText().isEmpty() || bDateField.getValue()==null) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Edit Error", "Some fields are empty", "All fields are mandatory.");
            return;
        }
        if(!currentUser.getLogin().equals(loginField.getText()) && userHib.checkForRepeatingNames(loginField.getText())!=null) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Login already exists", "Please choose another login");
            return;
        }
        else {
            user.setLogin(loginField.getText());
            user.setPassword(Hashing.hashing(passwordField.getText()));
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            user.setAddress(addressField.getText());
            user.setCardNo(cardNo.getText());
            user.setBday(bDateField.getValue());
            genericHib.update(user);

        }
        loadUsers();
    }
//----------------------------------open chart---------------------------------------------
    public void openChart(MouseEvent mouseEvent) {
        List<User> usersList = genericHib.getAllRecords(User.class);
        List<OrderItem> orderItemList = genericHib.getAllRecords(OrderItem.class);
        CategoryDataset dataset = createDatasetForUsers(usersList, orderItemList);
        JFreeChart chart = createBarChart(dataset);
        showChart(chart);
    }
    private CategoryDataset createDatasetForUsers(List<User> usersList, List<OrderItem> orderItemList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //gathering how many orders has user made
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
        JFrame frame = new JFrame("Bar Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));

        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //--------------------------------------------------------------------------------------
    public void editFeedback(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            feedbackTitle.setText(selectedReview.getCommentTitle());
            feedbackBody.setText(selectedReview.getCommentBody());
            feedSubmit.setDisable(false);
            feedbackDelButt.setDisable(true);
            feedbackEdit.setDisable(false);
            feedbackSendButt.setDisable(true);
        }
    }

    public void feedSubmition(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            selectedReview.setCommentTitle(feedbackTitle.getText());
            selectedReview.setCommentBody(feedbackBody.getText());
            genericHib.update(selectedReview);
            feedbackTitle.setText("");
            feedbackBody.setText("");
            feedSubmit.setDisable(true);
            feedbackDelButt.setDisable(false);
            feedbackEdit.setDisable(false);
            feedbackSendButt.setDisable(false);
            loadFeedback();
        }
    }

    public void selectUser(MouseEvent mouseEvent) {
        updateInfo.setDisable(true);
        userTableParameters selectedUser = userManagTableView.getSelectionModel().getSelectedItem();
        if (selectedUser!= null && selectedUser.getId()==currentUser.getId()) {
            loginField.setText(currentUser.getLogin());
            passwordField.setText(currentUser.getPassword());
            nameField.setText(currentUser.getName());
            surnameField.setText(currentUser.getSurname());
            addressField.setText(currentUser.getAddress());
            cardNo.setText(currentUser.getCardNo());
            bDateField.setValue(LocalDate.parse(selectedUser.getBirthDate()));
            updateInfo.setDisable(false);
        }
        else {
            loginField.setText("");
            passwordField.setText("");
            nameField.setText("");
            surnameField.setText("");
            addressField.setText("");
            cardNo.setText("");
            bDateField.setValue(null);
            updateInfo.setDisable(true);
    }
    }

    public void clearSortFields(MouseEvent mouseEvent) {
        userIDSort.setText("");
        orderStatusSort.setText("");
        dateFromSort.setValue(null);
        dateToSort.setValue(null);

    }
}
