
package com.supplychain.warehouse_service.specification;

import com.supplychain.warehouse_service.entity.Warehouse;
import com.supplychain.warehouse_service.entity.WarehouseStatus;
import org.springframework.data.jpa.domain.Specification;

public final class WarehouseSpecification {

    private WarehouseSpecification() {}

    public static Specification<Warehouse> hasCode(String code) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("code")),
                        "%" + code.toLowerCase() + "%"
                );
    }

    public static Specification<Warehouse> hasName(String name) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Warehouse> hasCity(String city) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("city")),
                        "%" + city.toLowerCase() + "%"
                );
    }

    public static Specification<Warehouse> hasState(String state) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("state")),
                        "%" + state.toLowerCase() + "%"
                );
    }

    public static Specification<Warehouse> hasCountry(String country) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("country")),
                        "%" + country.toLowerCase() + "%"
                );
    }

    public static Specification<Warehouse> hasPincode(String pincode) {
        return (root, query, cb) ->
                cb.equal(root.get("pincode"), pincode);
    }

    public static Specification<Warehouse> hasStatus(WarehouseStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Warehouse> capacityGreaterThanOrEqualTo(
            Integer minCapacity) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("capacity"),
                        minCapacity
                );
    }

    public static Specification<Warehouse> capacityLessThanOrEqualTo(
            Integer maxCapacity) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("capacity"),
                        maxCapacity
                );
    }

    public static Specification<Warehouse> utilizationGreaterThanOrEqualTo(
            Integer minCurrentUtilization) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("currentUtilization"),
                        minCurrentUtilization
                );
    }

    public static Specification<Warehouse> utilizationLessThanOrEqualTo(
            Integer maxCurrentUtilization) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("currentUtilization"),
                        maxCurrentUtilization
                );
    }
}