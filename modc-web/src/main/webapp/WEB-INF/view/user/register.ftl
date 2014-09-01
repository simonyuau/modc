<!DOCTYPE html>
<html>
<head>
<#include "../template/header.ftl"/>
    <title>User Register</title>
</head>
<body>
<!-- Navigation Section -->
<#include "../template/nav.ftl" />
<div style="clear:both"></div>
<div class="main_body_div">
    <div class="main_middle_div">

        <!-- left panel -->
        <div class="left_display_div">
            <div style="clear:both"></div>
            <div class="left_display_inner">
                <h3><@s.message "user.registration.title" /></h3>

            <!-- @sf.form action="register.do" method="post" -->
                <form action="register.do" method="post">
                <table>
                <@s.bind "user"/>
                        <tr>
                            <td>First Name :</td>
                            <td><@sf.input path="user.firstName" /></td>
                            <td><@sf.errors path="user.firstName" cssStyle="color : red;"  /></td>
                        </tr>

                        <tr>
                            <td>Last Name :</td>
                            <td><@sf.input path="user.lastName" /></td>
                            <td><@sf.errors path="user.lastName" cssStyle="color : red;" /></td>
                        </tr>
                    <tr>
                        <td>Email :</td>
                        <td><@sf.input path="user.email" /></td>
                        <td><@sf.errors path="user.email"  cssStyle="color : red;"/></td>
                    </tr>
                    <tr>
                        <td>Password :</td>
                        <td><@sf.password path="user.password" /></td>
                        <td><@sf.errors path="user.password"  cssStyle="color : red;"/></td>
                    </tr>
                <@s.bind "profile"/>
                    <tr>
                        <td>Organization :</td>
                        <td><@sf.input path="profile.organization" /></td>
                        <td><@sf.errors path="profile.organization"  cssStyle="color : red;"/></td>
                    </tr>
                    <tr>
                        <td colspan="3"><input type="submit" value="Register" class="input_button_norm"/></td>
                    </tr>
                </table>
            <!-- /@sf.form -->
                </form>
            </div>
        </div>
        <!-- right panel -->
        <div class="right_display_div">
            &nbsp;
        </div>
    </div>
</div>
</div>
<div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>