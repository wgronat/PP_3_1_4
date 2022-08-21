$(async function() {
    await userInfo()
});

async function userInfo() {
    fetch('http://localhost:8080/api/user')
        .then(res => res.json())
        .then(data => {
                    let tb = "";
                    tb += "<tr>";
                    tb += "<td>" + data.id + "</td>";
                    tb += "<td>" + data.firstName + "</td>";
                    tb += "<td>" + data.lastName + "</td>";
                    tb += "<td>" + data.age + "</td>";
                    tb += "<td>" + data.email + "</td>";
                    tb += "<td>" + data.roles.map(role => ' ' + role.name).join("") + "</td></tr>"

                    document.getElementById("userInfoEmail").innerHTML = data.email;
                    document.getElementById("userInfoRoles").innerHTML = data.roles.map(role => ' ' + role.name).join("");
                    document.getElementById("userInfo").innerHTML = tb;
        })
}