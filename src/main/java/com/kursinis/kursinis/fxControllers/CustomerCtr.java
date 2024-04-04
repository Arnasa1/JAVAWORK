package com.kursinis.kursinis.fxControllers;

import com.kursinis.kursinis.hibernateControllers.UserHib;
import com.kursinis.kursinis.model.*;
import com.kursinis.kursinis.hibernateControllers.GenericHib;
import com.kursinis.kursinis.tableParameters.orderTableParameters;
import com.kursinis.kursinis.tableParameters.productTableParameters;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class CustomerCtr implements Initializable {
    public UserHib userHib;
    public TextField commentTitleField;
    public TextField commentBodyField;
    public Button addComment;
    public Button editComment;
    public Button deleteComment;
    public Button addToCart;
    public Button submitButton;
    public ListView<Product> productCartList;
    public ListView<OrderItem> orderListView;
    public TextField loginField;
    public TextField passwordField;
    public TextField nameField;
    public TextField surnameField;
    public TextField addressField;
    public TextField cardNo;
    public DatePicker bdayTab;
    public TableView <orderTableParameters> orderTableView;
    public TreeView <Review> feedbackTreeView;
    public Button deleteFeedback;
    public ToggleGroup ratingRadio;
    public Button editFeedbackButton;
    public Button replyButton;
    public Button sendFeedback;
    public Button giveRatingButton;
    public TextField feedbackTitle;
    public TextField feedbackBody;
    public RadioButton dos;
    public RadioButton uno;
    public RadioButton tres;
    public RadioButton quattro;
    public RadioButton synco;
    public Button Submit;
    public TableColumn orderIDColumn;
    public TableColumn orderStatusColumn;
    public TableColumn dateColumn;
    public TableColumn ratingColumn;
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private GenericHib genericHib;

    @FXML
    public Tab productsTab;
    @FXML
    public TreeView <Comment> commentTreeView;

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
        loadProductData();
    }
    private void loadProductData() {
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
        loadProductData();
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
            selectedComment.getReplies().clear();
            genericHib.deleteComment(selectedComment.getId());
            loadComments();
        }
    }

    public void AddToCart(MouseEvent mouseEvent) {

        productTableParameters selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Product product = genericHib.getEntityById(Product.class, selectedProduct.getId());
        if (currentUser.getMyCart() == null) {
            Cart cart = new Cart();
            cart.setDateCreated(LocalDate.now());
            cart.setUser(currentUser);
            genericHib.create(cart);
            currentUser.setMyCart(cart);
            genericHib.update(currentUser);
            product.setCart(cart);
            genericHib.update(product);
        }
        else {
            Cart cart = currentUser.getMyCart();
            product.setCart(cart);
            genericHib.update(product);
        }
        loadProductData();
    }

    public void selectProduct(MouseEvent mouseEvent) {
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());
        loadComments();
    }
    public void loadComments() {
        Product selectedProduct = genericHib.getEntityById(Product.class, productTable.getSelectionModel().getSelectedItem().getId());
        commentTreeView.setRoot(new TreeItem<>());
        commentTreeView.setShowRoot(false);
        commentTreeView.getRoot().setExpanded(true);

        List<Comment> mainComments = selectedProduct.getComments().stream()
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

    public void cartTabLoaded(Event event) {
        loadCartTab();
        loadOrders();

    }

    public void removeProductFromCart(MouseEvent mouseEvent) {
        Product selectedProduct = productCartList.getSelectionModel().getSelectedItem();
        selectedProduct.setCart(null);
        genericHib.update(selectedProduct);
        loadCartTab();


    }

    public void loadCartTab() {
       Cart cart = genericHib.getEntityById(Cart.class, currentUser.getMyCart().getId());
        productCartList.getItems().clear();
        for(Product p : cart.getItemsInCart()) {
            productCartList.getItems().add(p);
        }
    }
    public void loadOrders(){
        orderListView.getItems().clear();
        List<OrderItem> orderList = genericHib.getAllRecords(OrderItem.class);
        for(OrderItem o : orderList) {
            if(o.getUser().getId() == currentUser.getId() && o.getOrderStatus().equals("Pending") || o.getOrderStatus().equals("Paid") || o.getOrderStatus().equals("Confirmed")) {
                orderListView.getItems().add(o);
            }
        }


    }
    public void createOrder(MouseEvent mouseEvent) {
        OrderItem order = new OrderItem();
        if(currentUser.getMyCart()!=null && currentUser.getMyCart().getItemsInCart()!=null) {
            order.setDateCreated(LocalDate.now());
            order.setUser(currentUser);
            order.setCart(currentUser.getMyCart());
            order.setOrderStatus("Pending");
            genericHib.create(order);
            Cart cart = currentUser.getMyCart();
            cart.setOrder(order);
            genericHib.update(cart);
            List <Product> productList = genericHib.getAllRecords(Product.class);
            for(Product p : productList) {
                if(p.getCart()!=null && p.getCart().getId() == cart.getId()) {
                    p.setCart(null);
                    genericHib.update(p);
                }
            }
        }

        loadOrders();
        loadCartTab();
    }

    public void selectOrder(MouseEvent mouseEvent) {

    }

    public void cancelOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        selectedOrder.setCart(null);
        selectedOrder.setOrderStatus("Cancelled");
        genericHib.update(selectedOrder);
        loadOrders();

    }

    public void payForOrder(MouseEvent mouseEvent) {
        OrderItem selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        selectedOrder.setOrderStatus("Paid");
        genericHib.update(selectedOrder);
        loadOrders();
    }


    public void loadYourInfoTab(Event event) {
    loadYourInfo();
    }
    public void loadYourInfo(){
        User user = genericHib.getEntityById(User.class, currentUser.getId());
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword());
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        addressField.setText(user.getAddress());
        cardNo.setText(user.getCardNo());
        bdayTab.setValue(user.getBday());


    }
    public void updateYourInfo(MouseEvent mouseEvent) {
        userHib = new UserHib(entityManagerFactory);
        User user = genericHib.getEntityById(User.class, currentUser.getId());
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty() || surnameField.getText().isEmpty() || addressField.getText().isEmpty() || cardNo.getText().isEmpty() || bdayTab.getValue()==null) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Edit Error", "Fields are empty", "All fields are required.");
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
            user.setBday(bdayTab.getValue());
            genericHib.update(user);
            loadYourInfo();
        }
    }


    public void loadOrderTab(Event event) {
        loadFeedbackOrders();

    }
    public void loadFeedbackOrders(){
        genericHib = new GenericHib(entityManagerFactory);
        List<OrderItem> orderItem = genericHib.getAllRecords(OrderItem.class);
        List<orderTableParameters> tableParametersList = new ArrayList<>();

        for (OrderItem order : orderItem) {
            if (order.getUser().getId()==currentUser.getId()){
                orderTableParameters tableParams = new orderTableParameters();
                tableParams.setId(order.getId());
                tableParams.setDateCreated(order.getDateCreated().toString());
                tableParams.setOrderStatus(order.getOrderStatus());
                tableParams.setRating(order.getRating());
                tableParametersList.add(tableParams);
            }
        }


        orderTableView.getItems().clear();
        orderTableView.getItems().addAll(tableParametersList);

        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

    }
    public void deleteFeedback(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            selectedReview.getReviewReply().clear();
            genericHib.deleteFeedback(selectedReview.getId());
            loadFeedback();
        }
    }


    public void editFeedback(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            feedbackTitle.setText(selectedReview.getCommentTitle());
            feedbackBody.setText(selectedReview.getCommentBody());
            Submit.setDisable(false);
            deleteFeedback.setDisable(true);
            editFeedbackButton.setDisable(false);
            replyButton.setDisable(true);
            sendFeedback.setDisable(true);
        }

    }


    public void sendFeedback(MouseEvent mouseEvent) {
        Review selectedReview = new Review();
        OrderItem order = genericHib.getEntityById(OrderItem.class, orderTableView.getSelectionModel().getSelectedItem().getId());
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


    public void giveRating(MouseEvent mouseEvent) {
        orderTableParameters order = orderTableView.getSelectionModel().getSelectedItem();
        OrderItem selectedOrder = genericHib.getEntityById(OrderItem.class, order.getId());
            if(dos.isSelected()) {
                selectedOrder.setRating(2);
                genericHib.update(selectedOrder);

            }
            else if(uno.isSelected()) {
                selectedOrder.setRating(1);
                genericHib.update(selectedOrder);

            }
            else if(tres.isSelected()) {
                selectedOrder.setRating(3);
                genericHib.update(selectedOrder);

            }
            else if(quattro.isSelected()) {
                selectedOrder.setRating(4);
                genericHib.update(selectedOrder);

            }
            else if(synco.isSelected()) {
                selectedOrder.setRating(5);
                genericHib.update(selectedOrder);

            }
            loadFeedbackOrders();
    }
    public void loadFeedback() {
        OrderItem order = genericHib.getEntityById(OrderItem.class, orderTableView.getSelectionModel().getSelectedItem().getId());
        feedbackTreeView.setRoot(new TreeItem<>());
        feedbackTreeView.setShowRoot(false);
        feedbackTreeView.getRoot().setExpanded(true);

        List<Review> mainReview = order.getReviews().stream()
                .filter(feedback -> feedback.getParentReview() == null)
                .collect(Collectors.toList());

        mainReview.forEach(feedback -> addTreeItem(feedback, feedbackTreeView.getRoot()));
    }

    private void addTreeItem(Review review, TreeItem<Review> parentFeedback) {
        TreeItem<Review> treeItem = new TreeItem<>(review);
        parentFeedback.getChildren().add(treeItem);
        if (review.getReviewReply() != null && !review.getReviewReply().isEmpty()) {
            List<Review> replies = review.getReviewReply().stream().filter(reply -> reply.getParentReview() != null && reply.getParentReview().equals(review)).collect(Collectors.toList());
            replies.forEach(reply -> addTreeItem(reply, treeItem));
        }
    }
    public void selectFeedback(MouseEvent mouseEvent) {
        submitButton.setDisable(true);
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() != currentUser.getId()) {
            deleteFeedback.setDisable(true);
            editFeedbackButton.setDisable(true);
            sendFeedback.setDisable(false);
            submitButton.setDisable(true);
        }
        else {
            deleteFeedback.setDisable(false);
            editFeedbackButton.setDisable(false);
            sendFeedback.setDisable(false);
            submitButton.setDisable(true);
        }

    }

    public void submitFeedback(MouseEvent mouseEvent) {
        Review selectedReview = feedbackTreeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedReview.getUser().getId() == currentUser.getId()) {
            selectedReview.setCommentTitle(feedbackTitle.getText());
            selectedReview.setCommentBody(feedbackBody.getText());
            genericHib.update(selectedReview);
            feedbackTitle.setText("");
            feedbackBody.setText("");
            Submit.setDisable(true);
            deleteFeedback.setDisable(false);
            editFeedbackButton.setDisable(true);
            replyButton.setDisable(false);
            sendFeedback.setDisable(false);
            loadFeedback();
        }
    }

    public void selectOrderFeed(MouseEvent mouseEvent) {
        orderTableParameters order = orderTableView.getSelectionModel().getSelectedItem();
        OrderItem selectedOrder = genericHib.getEntityById(OrderItem.class, order.getId());
        loadFeedback();
    }
}
