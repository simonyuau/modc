<!DOCTYPE html>
<html>
<head>
<#include "../template/header.ftl"/>
    <title>DOI Miniting Service</title>
</head>
<body>
<!-- Navigation Section -->
<#include "../template/nav.ftl" />

<!-- Navigation Title -->
<#include "../template/page_title.ftl" />
<!-- End of Navigation Title -->

<div class="main_body_div">
    <div class="main_middle_div">

        <!-- left panel -->
        <div class="left_display_div">
            <div style="clear:both"></div>
            <div class="left_display_inner">
            <#include "../template/action_message.ftl" />
                <h3><@s.message "doi.mint.title" /></h3>

                <!-- @sf.form action="register.do" method="post" -->
            <@sf.form action="mint.jspx" commandName="doiResource" method="post">
                <table>
                    <tr>
                        <td>URL:</td>
                        <td><@sf.input path="url" /></td>
                    </tr>
                    <#list doiResource.doiCreators as doiCreator>
                        <tr>
                            <td>Createor:</td>
                            <td>
                             <@sf.input path="doiCreators[${doiCreator_index}].creatorName" /></td>
                        </tr>
                    </#list>
                    <tr>
                        <td colspan="2"><input type="submit" value="Mint" class="input_button_norm"/></td>
                    </tr>
                </table>
            </@sf.form>
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