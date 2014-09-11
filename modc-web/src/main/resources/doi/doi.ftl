<?xml version="1.0" encoding="UTF-8"?>
<resource xmlns="http://datacite.org/schema/kernel-2.2"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://datacite.org/schema/kernel-2.2 http://schema.datacite.org/meta/kernel-2.2/metadata.xsd">
    <#if doi??>
    <identifier identifierType="DOI">${doi}</identifier>
    </#if>
    <creators>
        <creator>
            <creatorName>${creatorName}</creatorName>
        </creator>
    </creators>
    <titles>
        <title>${title}</title>
    </titles>
    <publisher>${publisher}</publisher>
    <publicationYear>${publicationYear}</publicationYear>
</resource>