package fr.dynamx.addons.basics.common.infos;

import com.jme3.math.Vector3f;
import fr.dynamx.addons.basics.BasicsAddon;
import fr.dynamx.addons.basics.common.modules.LicensePlateModule;
import fr.dynamx.addons.basics.utils.TextUtils;
import fr.dynamx.api.contentpack.object.part.BasePart;
import fr.dynamx.api.contentpack.object.part.IDrawablePart;
import fr.dynamx.api.contentpack.registry.*;
import fr.dynamx.api.entities.modules.ModuleListBuilder;
import fr.dynamx.client.renders.RenderPhysicsEntity;
import fr.dynamx.common.contentpack.type.vehicle.ModularVehicleInfo;
import fr.dynamx.common.entities.BaseVehicleEntity;
import fr.dynamx.common.entities.PackPhysicsEntity;

import javax.annotation.Nullable;

@RegisteredSubInfoType(
        name = "ImmatriculationPlate",
        registries = {SubInfoTypeRegistries.WHEELED_VEHICLES, SubInfoTypeRegistries.HELICOPTER},
        strictName = false
)
public class LicensePlateInfos extends BasePart<ModularVehicleInfo> implements IDrawablePart<BaseVehicleEntity<?>> {
    @PackFileProperty(configNames = "Rotation", type = DefinitionType.DynamXDefinitionTypes.VECTOR3F, description = "common.rotation")
    protected Vector3f rotation = new Vector3f();

    @PackFileProperty(configNames = "Pattern", required = false)
    protected String pattern = "aa-111-aa";

    @PackFileProperty(configNames = "Font", required = false)
    protected String font = BasicsAddon.ID + ":e";

    @PackFileProperty(configNames = "Color", description = "common.color", required = false)
    protected int[] color = new int[]{10, 10, 10};

    @PackFileProperty(configNames = "LineSpacing", required = false)
    protected float lineSpacing = 0.0F;

    @IPackFilePropertyFixer.PackFilePropertyFixer(registries = SubInfoTypeRegistries.WHEELED_VEHICLES)
    public static final IPackFilePropertyFixer PROPERTY_FIXER = (object, key, value) -> {
        if (key.startsWith("Immatriculation")) {
            key = key.replaceAll("Immatriculation", "");
            IPackFilePropertyFixer.FixResult superr = BasePart.PROPERTY_FIXER.fixInputField(object, key, value);
            return superr != null ? superr : new IPackFilePropertyFixer.FixResult(key, true);
        }
        return null;
    };

    public LicensePlateInfos(ModularVehicleInfo owner, String partName) {
        super(owner, partName);
    }

    @Override
    public void appendTo(ModularVehicleInfo owner) {
        owner.addPart(this);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public String getFont() {
        return font;
    }

    public int[] getColor() {
        return color;
    }

    public String getPattern() {
        return pattern;
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    @Override
    public void addModules(PackPhysicsEntity<?, ?> entity, ModuleListBuilder modules) {
        if (modules.hasModuleOfClass(LicensePlateModule.class)) { //Module yet added
            modules.getByClass(LicensePlateModule.class).addInformation(this);
        } else { //Module not yet added
            modules.add(new LicensePlateModule(this));
        }
    }

    @Override
    public String getName() {
        return "PlateInfo named " + getPartName() + " in " + getOwner().getName();
    }

    @Override
    public void drawParts(@Nullable BaseVehicleEntity<?> baseVehicleEntity, RenderPhysicsEntity<?> renderPhysicsEntity, ModularVehicleInfo modularVehicleInfo, byte b, float v) {
        if (baseVehicleEntity == null) {
            return;
        }
        LicensePlateModule module = baseVehicleEntity.getModuleByType(LicensePlateModule.class);
        for (LicensePlateInfos licensePlateInfos : modularVehicleInfo.getPartsByType(LicensePlateInfos.class)) {
            TextUtils.drawText(
                    licensePlateInfos.getPosition(),
                    licensePlateInfos.getScale(),
                    licensePlateInfos.getRotation(),
                    module.getPlate(),
                    licensePlateInfos.getColor(),
                    licensePlateInfos.getFont(),
                    licensePlateInfos.getLineSpacing());
        }
    }

    @Override
    public String[] getRenderedParts() {
        return new String[0];
    }
}