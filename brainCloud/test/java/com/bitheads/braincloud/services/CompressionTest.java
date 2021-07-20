package com.bitheads.braincloud.services;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Serge Brule on 21-07-20.
 */
public class CompressionTest extends TestFixtureNoAuth
{

    private final String _defaultEntityType = "placeholderText";
    private final String _defaultEntityValueName = "Lorem ipsum";
    private final String _defaultEntityValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada aliquet orci, vel ornare eros mollis non. Sed elementum dictum sapien, sit amet tincidunt diam hendrerit ac. Sed placerat ante auctor ex tempus, sit amet aliquet ipsum iaculis. Pellentesque eu turpis dapibus, venenatis leo et, luctus metus. Sed sodales ante eu felis bibendum ornare. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed tempor in ipsum et sodales. Ut posuere nibh aliquam nunc laoreet, a tincidunt mi dignissim. Phasellus volutpat diam bibendum, hendrerit dolor a, imperdiet nulla. Fusce malesuada leo sed est laoreet mattis. Mauris efficitur pretium augue non mollis. Quisque nec metus diam."
        + "Proin finibus sem non bibendum lacinia. In sodales id augue at tempus. Duis pulvinar vestibulum urna, vel placerat diam molestie eget. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed risus nisl, rutrum et eros condimentum, tempor facilisis est. Nulla a tempor massa. Phasellus luctus maximus finibus. Ut nec libero at nunc semper eleifend ac mollis velit."
        + "Aliquam scelerisque sem urna, ut pretium tortor ultricies sed. Proin at vestibulum nunc. Sed vitae pellentesque est, vitae faucibus mi. Vivamus quis dolor vitae eros fringilla pharetra. Suspendisse pharetra at mi nec imperdiet. Maecenas tincidunt quam nec lectus volutpat, vitae maximus turpis condimentum. In hac habitasse platea dictumst."
        + "Cras dapibus rutrum massa a dignissim. Etiam vulputate tincidunt venenatis. Maecenas turpis neque, molestie et nisl non, aliquam suscipit diam. Aenean non sem felis. Quisque eget elementum magna. Nulla neque tortor, vestibulum quis nisl sit amet, fermentum varius dolor. Praesent sit amet nunc eget neque volutpat ultricies quis eu tortor. Mauris ut consectetur odio."
        + "Fusce fermentum laoreet elit eu scelerisque. Nullam et orci sagittis, vehicula purus ut, euismod purus. Ut tincidunt suscipit nunc, a fermentum felis rhoncus ac. Proin at odio pharetra, tempor nisl non, posuere ante. Pellentesque malesuada gravida purus id porta. Nam tincidunt eget eros ultricies pretium. Phasellus condimentum, lectus a porttitor interdum, dolor nisl laoreet justo, et semper urna justo sit amet lorem. Nulla facilisi. Suspendisse eget nulla tristique, ullamcorper mauris quis, consequat urna. Donec bibendum laoreet diam, in vehicula elit pharetra vitae.";

    @Test
    public void testCompression() throws Exception
    {
       TestResult tr = new TestResult(_wrapper);
       
        _wrapper.getClient().getAuthenticationService().authenticateUniversal(getUser(Users.UserA).id, getUser(Users.UserA).password, true, tr);
        
        // Test with compression disabled
        _wrapper.getClient().getEntityService().createEntity(_defaultEntityType, Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue), null, tr);
        tr.Run();

        deleteAllDefaultEntities();

        _wrapper.getClient().getAuthenticationService().setCompressResponses(true);
        _wrapper.getClient().getEntityService().createEntity(_defaultEntityType, Helpers.createJsonPair(_defaultEntityValueName, _defaultEntityValue), null, tr);
        tr.Run();

        deleteAllDefaultEntities();
    }

    private void deleteAllDefaultEntities() {
        deleteAllDefaultEntities(1);
    }

    /// <summary>
    /// Deletes all default entities
    /// </summary>
    private void deleteAllDefaultEntities(int version) {
        TestResult tr = new TestResult(_wrapper);

        ArrayList<String> entityIds = new ArrayList<>(0);

        //get all entities
        _wrapper.getEntityService().getEntitiesByType(_defaultEntityType, tr);

        if (tr.Run()) {
            try {
                JSONArray entities = tr.m_response.getJSONObject("data").getJSONArray("entities");
                if (entities.length() <= 0) {
                    return;
                }

                for (int i = 0, ilen = entities.length(); i < ilen; ++i) {
                    entityIds.add(entities.getJSONObject(i).getString("entityId"));
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        while (!entityIds.isEmpty()) {
            tr.Reset();
            _wrapper.getEntityService().deleteEntity(entityIds.remove(0), version, tr);
            tr.Run();
        }
    }
}
