<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delete User</title>
    <!-- Latest compiled and minified CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- <link href="/css/demo.css" rel="stylesheet"> -->

</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-6 col-12 mx-auto">
                <h3>Delete user with id = ${id}</h3>
                <hr />

                <div class="alert alert-danger">
                    Are you sure to delete this user?
                </div>

                <!-- the modelAttribute is to pass data from view to controller -->
                <form:form method="post" action="/admin/user/delete" modelAttribute="newUser">
                <!-- the form input beloew is for controller to get user id from view through path attribute -->
                    <div class="mb-3" style="display: none;">
                        <label class="form-label">Id:</label>
                        <form:input value="${newUser.id}" type="text" class="form-control" path="id" />
                    </div>
                    <button class="btn btn-danger">Confirm</button>
                </form:form>
            </div>
        </div>
    </div>
</body>
</html>