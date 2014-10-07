$(document).ready(function () {
    $("#add_creator").click(function () {
        var rowIndex = $("#add_creator_tab > tbody > tr").length;
        var creatorHtml = "<tr>";
        creatorHtml += "<td>Creator:</td>";
        creatorHtml += "<td>";
        creatorHtml += "<div class='dvalue_row_div'>";
        creatorHtml += "<span class='dlabel_fixed_span'>Creator Name: </span> <input type='text' name='doiCreators[" + rowIndex + "].creatorName' id='creatorName'>";
        creatorHtml += " <span class='red_span'> *</span>";
        creatorHtml += " <span class='dcomment_span'>Required. Format: Family, Given.</span>";
        creatorHtml += "</div>";
        creatorHtml += "<div class='dvalue_row_div'>";
        creatorHtml += "<span class='dlabel_fixed_span'>Name Identifier: </span> <input type='text'  name='doiCreators[" + rowIndex + "].nameIdentifier.identifier' id='nameIdentifier'>";
        creatorHtml += " <span class='dcomment_span'>Optional, eg: ISNI identifier or ORCID.</span>";
        creatorHtml += "</div>";
        creatorHtml += "<div class='dvalue_row_div'>";
        creatorHtml += "<span class='dlabel_fixed_span'>Name Identifier Scheme: </span> <input type='text' class='dinput_small' name='doiCreators[" + rowIndex + "].nameIdentifier.nameIdentifierScheme' id='nameIdentifierScheme'>";
        creatorHtml += " <span class='dcomment_span'>Optional, eg: ISNI or ORCID.</span>";
        creatorHtml += "</div>";
        creatorHtml += "</td>";
        creatorHtml += "<td width='50'><img src='../images/delete.png' class='dfield_img' title='remove' id='remove_creator' /></td>";
        creatorHtml += "</tr>";
        $('#add_creator_tab > tbody:last').append(creatorHtml);

    });
});

$(document).on('click', "#remove_creator", function (event) {
    event.preventDefault();
    var trRowId = $(this).closest('tr');
    var trId = trRowId.attr('id');
    trRowId.remove();
    resortCreatorTabIndex();
});

function resortCreatorTabIndex() {
    var rowIndex = $("#add_creator_tab > tbody > tr").length;
    $('#add_creator_tab > tbody > tr').each(function () {
        var index = $(this).index();
        //creator name
        var creatorNameId = $(this).find('#creatorName');
        if (creatorNameId != null) {
            creatorNameId.attr('name', 'doiCreators[' + index + '].creatorName');
        }
        //name identifier id
        var nameIdentifierId = $(this).find('#nameIdentifier');
        if (nameIdentifierId != null) {
            nameIdentifierId.attr('name', 'doiCreators[' + index + '].nameIdentifier.identifier');
        }

        //name identifier scheme id
        var nameIdentifierSchemeId = $(this).find('#nameIdentifierScheme');
        if (nameIdentifierSchemeId != null) {
            nameIdentifierSchemeId.attr('name', 'doiCreators[' + index + '].nameIdentifier.nameIdentifierScheme');
        }
    });
}


$(document).ready(function () {
    $("#add_title").click(function () {
        var rowIndex = $("#add_title_tab > tbody > tr").length;
        var titleHtml = "<tr>";
        titleHtml += "<td>Title:</td>";
        titleHtml += "<td>";
        titleHtml += "<div class='dvalue_row_div'>";
        titleHtml += "<input type='text' name='titles[" + rowIndex + "].title' id='title'>";
        titleHtml += " <span class='red_span'> *</span>";
        titleHtml += " <span class='dcomment_span'>Required</span>";
        titleHtml += "</div>";
        titleHtml += "<div class='dvalue_row_div'>";
        titleHtml += "<select class='input_select_field' name='titles[" + rowIndex + "].titleType' id='titleType'>";
        titleHtml += "<option value='none'> --- Select --- </option>";
        titleHtml += "<option value='Subtitle'>Subtitle</option>";
        titleHtml += "<option value='AlternativeTitle'>Alternative Title</option>";
        titleHtml += "<option value='TranslatedTitle'>Translated Title</option>";
        titleHtml += "</select>";
        titleHtml += " <span class='dcomment_span'>Optional</span>";
        titleHtml += "</div>";
        titleHtml += "</td>";
        titleHtml += "<td width='50'><img src='../images/delete.png' class='dfield_img' title='remove' id='remove_title' /></td>";
        titleHtml += "</tr>";
        $('#add_title_tab > tbody:last').append(titleHtml);
    });
});


$(document).on('click', "#remove_title", function (event) {
    event.preventDefault();
    var trRowId = $(this).closest('tr');
    var trId = trRowId.attr('id');
    trRowId.remove();
    resortTitleTabIndex();
})

function resortTitleTabIndex() {
    $('#add_title_tab > tbody > tr').each(function () {
        //title
        var index = $(this).index();
        var titleId = $(this).find('#title');
        if (titleId != null) {
            titleId.attr('name', 'titles[' + index + '].title');
        }
        //title type
        var titleTypeId = $(this).find('#titleType');
        if (titleTypeId != null) {
            titleTypeId.attr('name', 'titles[' + index + '].titleType ');
        }
    });
}

//
//$(document).ready(function () {
//    alert("ready to start!");
//    var currentYear = (new Date()).getFullYear();
//    var select = $("#publication_year");
//    alert(select.attr('name'));
//    for(var i=2050; i >= 1900; i--){
//        var option =  $('<option>', {value: i, text: i});
//        if(i==currentYear){
//           option.attr('selected', 'selected');
//        }
//       option.appendTo(select);
//    }
//});