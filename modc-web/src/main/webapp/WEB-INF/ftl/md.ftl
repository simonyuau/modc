<#ftl strip_whitespace=true>
<#--
 * actionErrors
 *
 * Show Action errors for the currently with
 * optional style attributes.
 *
 * @param classOrStyle either the name of a CSS class element (which is defined in
 *    the template or an external CSS file) or an inline style. If the value passed in here
 *    contains a colon (:) then a 'style=' attribute will be used, else a 'class=' attribute
 *    will be used.
-->
<#macro actError actionErrors cssClassOrStyle="">
    <#if $actionErrors??>
    <div class="actionError">
        <ul>
            <#list actionErrors as error>
                <#if cssClassOrStyle =="">
                    <li><span class="errorMessage">${error}</span></li>
                <#else>
                    <#if cssClassOrStyle?index_of(":") ==-1>
                        <#assign  attr="class">
                    <#else>
                        <#assign attr="style">
                    </#if>
                    <li><span ${attr}="${cssClassOrStyle}">${error}</span></li>
                </#if>
            </#list>
        </ul>
    </div>
    </#if>
</#macro>


<#--
 * actionMessages
 *
 * Show action messages with
 * optional style attributes.
 *
 * @param classOrStyle either the name of a CSS class element (which is defined in
 *    the template or an external CSS file) or an inline style. If the value passed in here
 *    contains a colon (:) then a 'style=' attribute will be used, else a 'class=' attribute
 *    will be used.
-->
<#macro actMsg actionMessages="" cssClassOrStyle="">
    <#if actionMessages??>
    <div class="actionMessage">
        <ul>
            <#list actionMessages as actMessage>
                <#if cssClassOrStyle =="">
                    <li><span class="actMessage">${actMessage}</span></li>
                <#else>
                    <#if cssClassOrStyle?index_of(":") ==-1>
                        <#assign  attr="class">
                    <#else>
                        <#assign attr="style">
                    </#if>
                    <li><span ${attr}="${cssClassOrStyle}">${actMessage}</span></li>
                </#if>
            </#list>
        </ul>
    </div>
    </#if>
</#macro>