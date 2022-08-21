const url = 'http://localhost:8080/api/users/';

$(async function() {
    await allUsers();
    await editUser();
    await deleteUser();
    await addUser()
});

async function allUsers() {
    fetch(url).then(res => {
            res.json().then(
                data => {
                    if (data.length > 0) {
                        let tb = "";
                        data.forEach((user, index) => {
                            tb += "<tr>";
                            tb += "<td>" + user.id + "</td>";
                            tb += "<td>" + user.firstName + "</td>";
                            tb += "<td>" + user.lastName + "</td>";
                            tb += "<td>" + user.age + "</td>";
                            tb += "<td>" + user.email + "</td>";
                            tb += "<td>" + user.roles.map(role => ' ' + role.name).join("") + "</td>"
                            tb += "<td> <a  data-href='http://localhost:8080/api/users/" + user.id + "' class=\"btn btn-info btnEdit\" style='color:white' data-index=" + index + ">Edit</a>"
                                + "</td>";
                            tb += "<td> <a data-href='http://localhost:8080/api/users/" + user.id + "' class=\"btn btn-danger btnDelete\" style='color:white' data-index=" + index + ">Delete</a>"
                                + "</td></tr>";

                        })

                        editModal()
                        deleteModal()
                        document.getElementById("tbAllUsers").innerHTML = tb;
                    }
                }
            )
        }
    )
}

async function editModal() {
    $(document).ready(function () {
        $('.table .btnEdit').on('click', function (event) {
            event.preventDefault();
            let href = event.target.getAttribute('data-href')
            $.get(href, function (user) {
                $('.editForm #editId').val(user.id);
                $('.editForm #editFirstName').val(user.firstName);
                $('.editForm #editLastName').val(user.lastName);
                $('.editForm #editAge').val(user.age);
                $('.editForm #editEmail').val(user.email);
                $('.editForm #editPassword').val(user.password);
                $('.editForm #editRoles').val(user.roles);

            });
            $('.editForm #modalEdit').modal();
        })
    });
}

async function editUser() {
    const editForm = document.querySelector('.editUser')
    editForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        let id = document.querySelector('#editId');
        let firstName = document.querySelector('#editFirstName');
        let lastName = document.querySelector('#editLastName');
        let age = document.querySelector('#editAge')
        let email = document.querySelector('#editEmail');
        let password = document.querySelector('#editPassword');
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) editUserRoles.push({
                id : editForm.roles.options[i].value,
                name : editForm.roles.options[i].text,
                authority: editForm.roles.options[i].text
            })
        }

        try {
            await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: id.value,
                    firstName: firstName.value,
                    lastName: lastName.value,
                    age: age.value,
                    email: email.value,
                    password: password.value,
                    roles: editUserRoles
                })
            });

            $('#modalEdit').modal('hide');
            await allUsers();

        } catch (e) {
            console.error(e)
        }
    })
}
async function deleteModal(){
    $(document).ready(function () {
        $('.table .btnDelete').on('click', function (event) {
            event.preventDefault();
            let href = event.target.getAttribute('data-href')
            $.get(href, function (user) {
                $('.deleteForm #deleteId').val(user.id);
                $('.deleteForm #deleteFirstName').val(user.firstName);
                $('.deleteForm #deleteLastName').val(user.lastName);
                $('.deleteForm #deleteAge').val(user.age);
                $('.deleteForm #deleteEmail').val(user.email);
                $('.deleteForm #deletePassword').val(user.password);
                $('.deleteForm #deleteRoles').val(user.roles);
            });
            $('.deleteForm #modalDelete').modal();
        })
    })
}

async function deleteUser() {
    const deleteForm = document.querySelector('.deleteUser')
    deleteForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        let id = document.querySelector('#deleteId');
        try {
            await fetch(url + id.value, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            })

            $('#modalDelete').modal('hide');
            await allUsers();

        } catch (e) {
            console.error(e)
        }
    })
}

async function addUser(){
    const form = document.querySelector('.addUser')
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        let firstName = document.querySelector('#newFirstName');
        let lastName = document.querySelector('#newLastName');
        let age = document.querySelector('#newAge')
        let email = document.querySelector('#newEmail');
        let password = document.querySelector('#newPassword');
        let newUserRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) newUserRoles.push({
                id: form.roles.options[i].value,
                name: form.roles.options[i].name
            })
        }
        try {
             await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstName: firstName.value,
                    lastName: lastName.value,
                    age: age.value,
                    email: email.value,
                    password: password.value,
                    roles: newUserRoles
                })
            }).then(() => {
                 form.reset();
                 allUsers();
                 $('#mainTb li:first-child a').tab('show');
             })

        } catch (e) {
            console.error(e)
        }
    })
}

